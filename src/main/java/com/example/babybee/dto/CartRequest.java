package com.example.babybee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for carrying cart request details.
 * Used when adding items to the cart.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
    private String userEmail;
    private Long productId;
    private int quantity;
}
