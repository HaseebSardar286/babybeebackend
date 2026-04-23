package com.example.babybee.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing an individual item within an order.
 * Stores details such as the product name, price, and quantity.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    /** Unique identifier for the order item. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The name of the purchased product. */
    private String productName;

    /** The price of a single unit of the product. */
    private double price;

    /** The quantity of the product purchased. */
    private int quantity;
}
