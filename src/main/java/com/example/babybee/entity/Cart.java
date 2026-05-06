package com.example.babybee.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a shopping cart item for a user.
 */
@Entity
@Table(name = "cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    /** Unique identifier for the cart item. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Email of the user who owns this cart. */
    @Column(name = "user_email")
    private String userEmail;

    /** The product associated with this cart item. */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    /** The quantity of the product in the cart. */
    private int quantity;

}
