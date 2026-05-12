package com.example.babybee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.babybee.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT * FROM categories", nativeQuery = true)
    List<Category> findAllNative();

    @Query(value = "SELECT * FROM categories WHERE slug = :slug", nativeQuery = true)
    Optional<Category> findBySlugNative(@Param("slug") String slug);

}
