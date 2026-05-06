package com.example.babybee.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.babybee.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing Order entities.
 * Extends JpaRepository to provide standard CRUD operations.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves a list of orders associated with a specific user email using a
     * native SQL query.
     *
     * @param email the email of the user
     * @return a list of Order objects belonging to the user
     */
    @Query(value = "SELECT * FROM orders WHERE user_email = :email", nativeQuery = true)
    List<Order> findByUserEmail(@Param("email") String email);
}
