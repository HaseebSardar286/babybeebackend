package com.example.babybee.dto;

import lombok.Data;

/**
 * Data Transfer Object for product creation and modification requests.
 */
@Data
public class ProductRequest {
    private String name;
    private String category;
    private double price;
    private String description;
    private int stock;
}
