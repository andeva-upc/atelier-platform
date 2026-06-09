package com.andeva.atelier.platform.inventory.domain.model.valueobjects;

public record ProductCategory(String value) {
    public ProductCategory {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Product category cannot be null or empty");
        }
    }
}
