package com.example.babybee.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A basic REST controller used for testing authentication and role authorizations.
 */
@RestController
@RequestMapping("/api")
public class TestController {
    /**
     * A test endpoint accessible to users with the USER role.
     *
     * @param auth the current authenticated user
     * @return a simple test string confirming user access
     */
    @GetMapping("/user/test")
    public String userTest(Authentication auth) {
        return "User access: " + auth.getName();
    }

    /**
     * A test endpoint accessible only to users with the ADMIN role.
     *
     * @param auth the current authenticated admin user
     * @return a simple test string confirming admin access
     */
    @GetMapping("/admin/test")
    public String adminTest(Authentication auth) {
        return "Admin access: " + auth.getName();
    }
}
