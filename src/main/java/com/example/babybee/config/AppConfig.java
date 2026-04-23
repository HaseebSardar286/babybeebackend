package com.example.babybee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration class for the application.
 * Defines Spring beans that can be injected across the application.
 */
@Configuration
public class AppConfig {
    /**
     * Creates and configures a BCryptPasswordEncoder bean.
     * Used for securely hashing and verifying user passwords.
     * 
     * @return a new instance of BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
