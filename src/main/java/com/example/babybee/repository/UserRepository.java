package com.example.babybee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.babybee.entity.User;

/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their email address.
     *
     * @param email the email to search for
     * @return an Optional containing the user if found, or empty otherwise
     */
    Optional<User> findByEmail(String email);
}
