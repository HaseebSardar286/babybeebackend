package com.example.babybee.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.babybee.dto.CartRequest;
import com.example.babybee.entity.Cart;
import com.example.babybee.entity.Product;
import com.example.babybee.repository.CartRepository;
import com.example.babybee.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service responsible for handling operations related to user shopping carts.
 */
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    /**
     * Adds a specific product and its quantity to the given user's cart.
     *
     * @param userEmail the email of the user
     * @param request   the requested cart details (product id and quantity)
     * @return the newly saved cart item
     */
    public Cart addToCart(String userEmail, CartRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Cart cart = Cart.builder()
                .userEmail(userEmail)
                .product(product)
                .quantity(request.getQuantity())
                .build();
        return cartRepository.save(cart);
    }

    /**
     * Retrieves all products currently stored in the specific user's cart.
     *
     * @param userEmail the user email
     * @return list of items in the cart
     */
    public List<Cart> getCartItems(String userEmail) {
        return cartRepository.findByUserEmail(userEmail);
    }

    /**
     * Removes a cart item by its ID, ensuring it belongs to the authenticated user.
     *
     * @param cartItemId the ID of the cart item to remove
     * @param userEmail  the email of the authenticated user
     */
    public void removeFromCart(Long cartItemId, String userEmail) {
        Cart cart = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!cart.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized");
        }
        cartRepository.deleteById(cartItemId);
    }

    /**
     * Updates the quantity of an existing cart item.
     *
     * @param cartItemId the ID of the cart item
     * @param quantity   the new quantity
     * @param userEmail  the email of the authenticated user
     * @return the updated cart item
     */
    public Cart updateQuantity(Long cartItemId, int quantity, String userEmail) {
        Cart cart = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        if (!cart.getUserEmail().equals(userEmail)) {
            throw new RuntimeException("Unauthorized");
        }
        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }
}
