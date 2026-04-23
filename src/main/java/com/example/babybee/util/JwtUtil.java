package com.example.babybee.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * Utility class for handling JSON Web Tokens (JWT).
 * Provides methods to generate, extract details from, and validate tokens.
 */
@Component
public class JwtUtil {
    private final String SECRET = "mysecretkeymysecretkeymysecretkey123456";

    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Generates a new JWT token for the specified user email and role.
     *
     * @param email the email of the user
     * @param role  the role of the user
     * @return the generated JWT token string
     */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the user email (subject) from a given JWT token.
     *
     * @param token the JWT token
     * @return the extracted email
     */
    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extracts the user role from a given JWT token.
     *
     * @param token the JWT token
     * @return the extracted role
     */
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
