package com.andeva.atelier.platform.inventory.interfaces.rest.resources;

import java.util.List;

public record ProductDetailsResource(
        String id,
        String branchId,
        String category,
        String name,
        String sku,
        String description,
        Double salePrice,
        Integer currentStock,
        Integer reservedStock,
        Integer minimumStock,
        List<ProductBatchResource> batches
) {
}
