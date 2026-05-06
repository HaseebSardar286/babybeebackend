package com.example.babybee.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.babybee.entity.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing Cart entities.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Finds all cart items associated with a given user's email using a native SQL query.
     */
    @Query(value = "SELECT * FROM cart WHERE user_email = :email", nativeQuery = true)
    List<Cart> findByUserEmail(@Param("email") String email);
}
