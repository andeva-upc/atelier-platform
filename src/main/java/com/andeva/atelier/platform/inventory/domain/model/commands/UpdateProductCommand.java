package com.andeva.atelier.platform.inventory.domain.model.commands;

import com.andeva.atelier.platform.inventory.domain.model.valueobjects.ProductCategory;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.ProductName;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.Sku;

import java.util.UUID;

public record UpdateProductCommand(
        UUID productId,
        ProductName name,
        ProductCategory category,
        Sku sku
) {
}
