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
    private int stock;

    /** The category to which the product belongs. */
    private String category;

    /** URL for the product image. */
    @Column(name = "image_url")
    private String imageUrl;

    /** Whether the product is available for sale. */
    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;

    /** Timestamp when the product was created. */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when the product was last updated. */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryEntity;

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