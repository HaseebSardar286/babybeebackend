package com.example.babybee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entity representing a product in the system.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /** Unique identifier for the product. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the product. */
    @Column(nullable = false)
    private String name;

    /** Description of the product. */
    private String description;

    /** Price of the product. */
    @Column(nullable = false)
    private double price;

    /** Available quantity of the product in stock. */
    private int quantity;

    /** The category to which the product belongs. */
    private String category;

    /** Timestamp when the product was created. */
    private LocalDateTime createdAt;
    
    /** Timestamp when the product was last updated. */
    private LocalDateTime updatedAt;

    /**
     * Automatically sets createdAt and updatedAt before persisting.
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Automatically sets updatedAt before updating.
     */
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}