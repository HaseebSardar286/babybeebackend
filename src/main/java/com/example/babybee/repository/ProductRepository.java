package com.example.babybee.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.babybee.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing Product entities.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    @Query(value = "SELECT * FROM products WHERE LOWER(name) LIKE LOWER(CONCAT('%', :keyword, '%'))", nativeQuery = true)
    List<Product> findByNameIgnoreCaseContaining(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM products WHERE LOWER(category) LIKE LOWER(CONCAT('%', :category, '%'))", nativeQuery = true)
    List<Product> findByCategoryIgnoreCaseContaining(@Param("category") String category);

    @Query(value = "SELECT * FROM products", nativeQuery = true)
    Page<Product> findAll(Pageable pageable);
}
