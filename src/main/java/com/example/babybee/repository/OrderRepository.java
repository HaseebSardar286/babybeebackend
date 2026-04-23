package com.example.babybee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.babybee.entity.Order;

/**
 * Repository interface for managing Order entities.
 * Extends JpaRepository to provide standard CRUD operations.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Retrieves a list of orders associated with a specific user email.
     *
     * @param email the email of the user
     * @return a list of Order objects belonging to the user
     */
    List<Order> findByUserEmail(String email);
}
