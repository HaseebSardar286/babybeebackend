package com.example.babybee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.babybee.entity.Cart;

/**
 * Repository interface for managing Cart entities.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Finds all cart items associated with a given user's email.
     *
     * @param userEmail the email of the user
     * @return a list of cart items belonging to the user
     */
    List<Cart> findByUserEmail(String userEmail);
}
