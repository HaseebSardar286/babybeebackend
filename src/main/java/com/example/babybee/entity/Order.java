package com.example.babybee.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a user's order.
 * Contains information about the user, total amount, order status, and a list of order items.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    /** Unique identifier for the order. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Email of the user who placed the order. */
    private String userEmail;

    /** The total monetary amount of the order. */
    private double totalAmount;

    /** 
     * The current status of the order.
     * e.g., PENDING, CONFIRMED, SHIPPED
     */
    private String status;

    /** The date and time when the order was created. */
    private LocalDateTime createdAt;

    /** The list of items included in this order. */
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;

    /**
     * Lifecycle callback method executed before the entity is persisted.
     * Initializes the creation timestamp and sets the default status to PENDING.
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }
}
