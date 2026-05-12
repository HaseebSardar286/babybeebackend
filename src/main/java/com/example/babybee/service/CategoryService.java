package com.example.babybee.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.example.babybee.entity.Category;
import com.example.babybee.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    public final JdbcTemplate jdbcTemplate;

    public void createCategory(String name, String slug, Long parentId) {
        String sql = "INSERT INTO categories (name, slug, parent_id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, name, slug, parentId);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAllNative();
    }

}
