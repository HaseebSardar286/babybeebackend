package com.example.babybee.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.babybee.dto.ApiResponse;
import com.example.babybee.dto.CartRequest;
import com.example.babybee.entity.Cart;
import com.example.babybee.service.CartService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * REST controller for managing the user's shopping cart.
 * Provides endpoints to add, update, remove, and retrieve cart items.
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     * Adds a product to the authenticated user's cart.
     */
    @PostMapping("/add")
    public ApiResponse<Cart> addToCart(@RequestBody CartRequest request, Authentication authentication) {
        String email = authentication.getName();
        Cart cart = cartService.addToCart(email, request);
        return ApiResponse.<Cart>builder()
                .message("Item added to cart successfully")
                .success(true)
                .data(cart)
                .build();
    }

    /**
     * Retrieves all cart items for the authenticated user.
     */
    @GetMapping
    public ApiResponse<List<Cart>> getCartItems(Authentication authentication) {
        String email = authentication.getName();
        List<Cart> cartItems = cartService.getCartItems(email);
        return ApiResponse.<List<Cart>>builder()
                .message("Cart items fetched successfully")
                .success(true)
                .data(cartItems)
                .build();
    }

    /**
     * Removes a specific cart item by its ID.
     */
    @DeleteMapping("/{cartItemId}")
    public ApiResponse<Void> removeFromCart(@PathVariable Long cartItemId, Authentication authentication) {
        String email = authentication.getName();
        cartService.removeFromCart(cartItemId, email);
        return ApiResponse.<Void>builder()
                .message("Item removed from cart")
                .success(true)
                .build();
    }

    /**
     * Updates the quantity of a specific cart item.
     */
    @PatchMapping("/{cartItemId}")
    public ApiResponse<Cart> updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity,
            Authentication authentication) {
        String email = authentication.getName();
        Cart updated = cartService.updateQuantity(cartItemId, quantity, email);
        return ApiResponse.<Cart>builder()
                .message("Quantity updated")
                .success(true)
                .data(updated)
                .build();
    }
}
