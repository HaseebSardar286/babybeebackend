package com.example.babybee.service;

import com.example.babybee.entity.*;
import com.example.babybee.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.management.RuntimeErrorException;

/**
 * Service responsible for processing orders.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    /**
     * Processes an order by converting a user's cart into an Order entity.
     * It computes the total amount, generates order items, saves the order,
     * and finally empties the user's cart.
     *
     * @param userEmail the user's email address placing the order
     * @return the saved database Order entity
     */
    public Order placeOrder(String userEmail) {

        List<Cart> cartItems = cartRepository.findByUserEmail(userEmail);

        double total = cartItems.stream()
                .mapToDouble(cart -> cart.getProduct().getPrice() * cart.getQuantity())
                .sum();

        List<OrderItem> items = cartItems.stream().map(cart -> OrderItem.builder()
                .productName(cart.getProduct().getName())
                .price(cart.getProduct().getPrice())
                .quantity(cart.getQuantity())
                .build()).toList();

        Order order = Order.builder()
                .userEmail(userEmail)
                .totalAmount(total)
                .items(items)
                .build();

        cartRepository.deleteAll(cartItems);

        return orderRepository.save(order);
    }

    /**
     * Retrieves the history of orders made by a specific user.
     *
     * @param userEmail the given user's email address
     * @return visually representation model of an exact list of orders
     */
    public List<Order> getOrders(String userEmail) {
        return orderRepository.findByUserEmail(userEmail);
    }

    /**
     * Updates the status of a specific order.
     * 
     * @param orderId the given order's id
     * @param status  the updated status
     * @return the updated Order entity
     */
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found!"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    /**
     * Retrieves all orders made.
     * 
     * @return the visually representation model of an exact list of all orders
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

}