package com.example.babybee.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.babybee.dto.ApiResponse;
import com.example.babybee.dto.AuthResponse;
import com.example.babybee.dto.LoginRequest;
import com.example.babybee.dto.RegisterRequest;
import com.example.babybee.entity.User;
import com.example.babybee.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import jakarta.annotation.PostConstruct;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for handling user authentication and registration logic using
 * raw SQL.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    /**
     * Automatically inspects the users database schema and adds reset_code / reset_code_expiry
     * columns if they are not already present.
     */
    @PostConstruct
    public void initDatabaseSchema() {
        try {
            String checkSql = "SELECT COUNT(*) FROM information_schema.columns WHERE table_name='users' AND column_name='reset_code'";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class);
            if (count == null || count == 0) {
                System.out.println("Migrating database: Adding reset_code and reset_code_expiry to users table...");
                jdbcTemplate.execute("ALTER TABLE users ADD COLUMN reset_code VARCHAR(255)");
                jdbcTemplate.execute("ALTER TABLE users ADD COLUMN reset_code_expiry TIMESTAMP");
                System.out.println("Database migration completed successfully!");
            }
        } catch (Exception e) {
            System.err.println("WARNING: Database migration failed or columns already exist: " + e.getMessage());
        }
    }

    /**
     * Registers a new user account using raw SQL.
     */
    public ApiResponse<User> register(RegisterRequest request) {
        // Check if user exists using SQL
        String checkSql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, request.getEmail());

        if (count != null && count > 0) {
            return new ApiResponse<User>("User Already Exists", false, null);
        }

        // Insert using SQL
        String insertSql = "INSERT INTO users (name, email, password, role, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertSql,
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                "USER",
                "ACTIVE",
                LocalDateTime.now(),
                LocalDateTime.now());

        return new ApiResponse<User>("User Registered Successfully", true, null);
    }

    /**
     * Authenticates a user using raw SQL.
     */
    public AuthResponse login(LoginRequest request) {
        try {
            String selectSql = "SELECT * FROM users WHERE email = ?";
            User user = jdbcTemplate.queryForObject(selectSql, new BeanPropertyRowMapper<>(User.class),
                    request.getEmail());

            if (user == null) {
                return new AuthResponse(null, "User Not Found");
            }
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return new AuthResponse(null, "Invalid Password");
            }
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            return new AuthResponse(token, "Login Success");
        } catch (Exception e) {
            System.err.println("CRITICAL LOGIN ERROR: " + e.getMessage());
            return new AuthResponse(null, "User Not Found or Internal Error");
        }
    }

    private String generateSecureOtp() {
        SecureRandom random = new SecureRandom();
        int num = 100000 + random.nextInt(900000);
        return String.valueOf(num);
    }

    public AuthResponse forgotPassword(String email) {
        // Check if user exists using SQL to avoid EmptyResultDataAccessException from queryForObject
        String checkSql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, email);
        if (count == null || count == 0) {
            throw new RuntimeException("User Not Found");
        }

        // Retrieve user
        String selectSql = "SELECT * FROM users WHERE email = ?";
        User user = jdbcTemplate.queryForObject(selectSql, new BeanPropertyRowMapper<>(User.class), email);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }
        
        // Generate short clean 6-digit OTP code and set its 15 minutes expiration
        String otp = generateSecureOtp();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(15);
        
        // Set status to INACTIVE and store verification details in database
        String updateSql = "UPDATE users SET status = ?, reset_code = ?, reset_code_expiry = ? WHERE email = ?";
        jdbcTemplate.update(updateSql, "INACTIVE", otp, expiry, email);
        
        // Send the 6-digit OTP code in the email link (throws RuntimeException on SMTP failure)
        emailService.sendResetToken(user.getEmail(), otp);
        
        // Return null for the token to match production security
        return new AuthResponse(null, "Reset password link has been sent to your email");
    }

    /**
     * Resets a user's password using the provided reset code.
     */
    public ApiResponse<String> resetPassword(String code, String newPassword) {
        try {
            // Find the user by the active reset code
            String selectSql = "SELECT email FROM users WHERE reset_code = ? AND reset_code_expiry > ?";
            List<String> emails = jdbcTemplate.query(selectSql, (rs, rowNum) -> rs.getString("email"), code, LocalDateTime.now());
            
            if (emails.isEmpty()) {
                return new ApiResponse<>("Invalid or expired verification code", false, null);
            }
            String email = emails.get(0);

            // Update password, clear reset details, and reactivate user account
            String updateSql = "UPDATE users SET password = ?, status = ?, reset_code = NULL, reset_code_expiry = NULL, updated_at = ? WHERE email = ?";
            jdbcTemplate.update(updateSql, 
                passwordEncoder.encode(newPassword), 
                "ACTIVE", 
                LocalDateTime.now(), 
                email
            );

            return new ApiResponse<>("Password reset successfully. You can now log in.", true, "SUCCESS");
        } catch (Exception e) {
            System.err.println("ERROR RESETTING PASSWORD: " + e.getMessage());
            return new ApiResponse<>("Invalid or expired verification code", false, null);
        }
    }
}

