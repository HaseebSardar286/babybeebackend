package com.example.babybee.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.babybee.dto.ApiResponse;
import com.example.babybee.dto.AuthResponse;
import com.example.babybee.dto.LoginRequest;
import com.example.babybee.dto.RegisterRequest;
import com.example.babybee.entity.User;
import com.example.babybee.repository.UserRepository;
import com.example.babybee.util.JwtUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class for handling user authentication and registration logic.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Registers a new user account.
     *
     * @param request the registration details
     * @return the API response indicating the outcome
     */
    public ApiResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ApiResponse("User Already Exists", false, null);
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setStatus(request.getStatus());

        userRepository.save(user);
        return new ApiResponse("User Registered Successfully", true, null);
    }

    /**
     * Authenticates a user and generates a JWT token if successful.
     *
     * @param request the login credentials
     * @return the AuthResponse with token or an error message
     */
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user == null) {
            return new AuthResponse(null, "User Not Found");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return new AuthResponse(null, "Invalid Password");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        return new AuthResponse(token, "Login Success");
    }
}
