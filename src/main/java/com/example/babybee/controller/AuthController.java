package com.example.babybee.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.babybee.dto.ApiResponse;
import com.example.babybee.dto.AuthResponse;
import com.example.babybee.dto.LoginRequest;
import com.example.babybee.dto.RegisterRequest;
import com.example.babybee.service.AuthService;
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
    public ApiResponse register(@RequestBody RegisterRequest request) {
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

}
