package com.example.babybee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.example.babybee.entity.Category;
import com.example.babybee.service.CategoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/admin/create")
    public ResponseEntity<String> create(@RequestParam String name,
            @RequestParam String slug,
            @RequestParam(required = false) Long parentId) {
        categoryService.createCategory(name, slug, parentId);
        return ResponseEntity.ok("Category created successfully");
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

}
