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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * REST controller for managing the user's shopping cart.
 * Provides endpoints to add items to the cart and retrieve cart items.
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    /**
     * Adds a product to the authenticated user's cart.
     *
     * @param request        the cart request containing product details and
     *                       quantity
     * @param authentication the current authenticated user
     * @return the saved Cart item
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
     *
     * @param authentication the current authenticated user
     * @return a list of Cart items belonging to the user
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

}
