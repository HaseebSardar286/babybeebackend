package com.example.babybee.controller;

import com.example.babybee.dto.ApiResponse;
import com.example.babybee.entity.Order;
import com.example.babybee.service.OrderService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing orders.
 * Provides endpoints for placing and retrieving user orders.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Endpoint to place a new order for the currently authenticated user.
     *
     * @param authentication the authentication token containing the user's details
     * @return the created Order object
     */
    @PostMapping("/place")
    public ApiResponse<Order> placeOrder(Authentication authentication) {
        Order order = orderService.placeOrder(authentication.getName());
        return ApiResponse.<Order>builder()
                .message("Order placed successfully")
                .success(true)
                .data(order)
                .build();
    }

    /**
     * Endpoint to retrieve all orders belonging to the currently authenticated
     * user.
     *
     * @param authentication the authentication token containing the user's details
     * @return a list of Order objects associated with the user
     */
    @GetMapping
    public ApiResponse<List<Order>> getOrders(Authentication authentication) {
        List<Order> orders = orderService.getOrders(authentication.getName());
        return ApiResponse.<List<Order>>builder()
                .message("Orders fetched successfully")
                .success(true)
                .data(orders)
                .build();
    }

    @GetMapping("/admin/allOrders")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ApiResponse.<List<Order>>builder()
                .message("Orders fetched successfully")
                .success(true)
                .data(orders)
                .build();
    }

    @PutMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Order> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String status = payload.get("status");
        Order order = orderService.updateOrderStatus(id, status);
        return ApiResponse.<Order>builder()
                .message("Order status updated successfully")
                .success(true)
                .data(order)
                .build();
    }
}
