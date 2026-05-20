package com.example.babybee.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.babybee.dto.ForgotPasswordRequest;
import com.example.babybee.dto.ResetPasswordRequest;
import com.example.babybee.dto.ApiResponse;
import com.example.babybee.dto.AuthResponse;
import com.example.babybee.dto.LoginRequest;
import com.example.babybee.dto.RegisterRequest;
import com.example.babybee.entity.User;
import com.example.babybee.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for handling authentication operations.
 * Provides endpoints for user registration and login.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Registers a new user with the provided registration details.
     *
     * @param request the registration request containing user details
     * @return an ApiResponse indicating the outcome of the registration
     */
    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param request the login request containing email and password
     * @return an AuthResponse containing the JWT token if successful
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    /**
     * Endpoint to initiate a password reset process.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            AuthResponse response = authService.forgotPassword(request.getEmail());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if ("User Not Found".equals(message)) {
                status = HttpStatus.NOT_FOUND;
            } else if (message != null && message.contains("SMTP")) {
                status = HttpStatus.BAD_GATEWAY;
            }
            return ResponseEntity.status(status).body(new AuthResponse(null, message));
        }
    }

    /**
     * Endpoint to complete the password reset process.
     */
    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        return authService.resetPassword(request.getToken(), request.getNewPassword());
    }

}

