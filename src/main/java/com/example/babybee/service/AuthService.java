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
import java.time.LocalDateTime;

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
}
