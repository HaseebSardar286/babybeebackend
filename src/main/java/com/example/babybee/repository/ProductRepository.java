package com.example.babybee.repository;

import java.util.List;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.babybee.entity.Product;

/**
 * Repository interface for managing Product entities.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameIgnoreCaseContaining(String keyword);

    List<Product> findByCategoryIgnoreCaseContaining(String category);

    Page<Product> findAll(Pageable pageable);
}
