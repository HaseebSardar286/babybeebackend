package com.example.babybee.service;

import java.util.List;

import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.babybee.dto.ProductRequest;
import com.example.babybee.entity.Product;
import com.example.babybee.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

/**
 * Service for working with Product data.
 * Allows adding new items and retrieving product lists to show to clients.
 */
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * Create and adds a specific product object towards the database repository.
     *
     * @param productRequest DTO carrying exact product definitions.
     * @return Final saved product inside of DB representations.
     */
    public Product addProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .category(productRequest.getCategory())
                .price(productRequest.getPrice())
                .description(productRequest.getDescription())
                .quantity(productRequest.getQuantity())
                .build();

        return productRepository.save(product);
    }

    /**
     * Fetches up a list containing all the individual active items in DB.
     *
     * @return the fetched list arrays to stream down towards frontend
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return productRepository.findAll();
        }
        String lowerCaseKeyword = keyword.toLowerCase();
        return productRepository.findByNameIgnoreCaseContaining(lowerCaseKeyword);
    }

    public List<Product> filterProductsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            return productRepository.findAll();
        }
        String lowerCaseCategory = category.toLowerCase();
        return productRepository.findByCategoryIgnoreCaseContaining(lowerCaseCategory);
    }

    public Page<Product> getProductsPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

}
