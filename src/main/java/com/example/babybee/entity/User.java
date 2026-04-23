package com.example.babybee.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity representing a user in the system.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /** Unique identifier for the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /** Full name of the user. */
    private String name;
    
    /** Unique email address of the user, used for logging in. */
    @Column(unique = true, nullable = false)
    private String email;
    
    /** Encrypted password for authentication. */
    @Column(nullable = false)
    private String password;
    
    /** The role assigned to the user (e.g., USER, ADMIN). */
    private String role;
    
    /** The active status of the user account. */
    private String status;
    
    /** Timestamp of when the user account was created. */
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    /** Timestamp of the last update to the user account. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Automatically populates createdAt and updatedAt constraints before persisting.
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Automatically updates the updatedAt field before updating.
     */
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
