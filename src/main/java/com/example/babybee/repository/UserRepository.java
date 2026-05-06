package com.example.babybee.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.babybee.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their email address using a native SQL query.
     *
     * @param email the email to search for
     * @return an Optional containing the user if found, or empty otherwise
     */
    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
}
