package com.andeva.atelier.platform.inventory.interfaces.rest.resources;

public record UpdateProductResource(
        String name,
        String category,
        String sku
) {
}
