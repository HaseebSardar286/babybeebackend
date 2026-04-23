package com.example.babybee.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.babybee.dto.ApiResponse;
import com.example.babybee.dto.ProductRequest;
import com.example.babybee.entity.Product;
import com.example.babybee.service.ProductService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST controller for managing products.
 * Provides endpoints to retrieve products and allows admins to add new
 * products.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * Adds a new product to the system.
     * Requires the user to have the ADMIN role.
     *
     * @param productRequest the product details to add
     * @return the created Product
     */
    @PostMapping("/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    public Product addProduct(@RequestBody ProductRequest productRequest) {
        return productService.addProduct(productRequest);
    }

    /**
     * Retrieves a list of all available products.
     *
     * @return a list of all Product entities
     */
    @GetMapping("/products")
    public ApiResponse<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ApiResponse.<List<Product>>builder()
                .message("Products fetched successfully")
                .success(true)
                .data(products)
                .build();
    }

    @GetMapping("/products/search")
    public ApiResponse<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = productService.searchProducts(keyword);
        return ApiResponse.<List<Product>>builder()
                .message("Products fetched successfully")
                .success(true)
                .data(products)
                .build();
    }

    @GetMapping("/products/filter")
    public ApiResponse<List<Product>> filterProductsByCategory(@RequestParam String category) {
        List<Product> products = productService.filterProductsByCategory(category);
        return ApiResponse.<List<Product>>builder()
                .message("Products fetched successfully")
                .success(true)
                .data(products)
                .build();
    }

    @GetMapping("/products/page")
    public ApiResponse<Page<Product>> getProductsPage(Pageable pageable) {
        Page<Product> products = productService.getProductsPage(pageable);
        return ApiResponse.<Page<Product>>builder()
                .message("Products fetched successfully")
                .success(true)
                .data(products)
                .build();
    }

}
