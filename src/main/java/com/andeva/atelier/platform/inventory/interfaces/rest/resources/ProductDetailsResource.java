package com.andeva.atelier.platform.inventory.interfaces.rest.resources;

import java.util.List;

public record ProductDetailsResource(
        String id,
        String branchId,
        String category,
        String name,
        String sku,
        Integer currentStock,
        Integer reservedStock,
        List<ProductBatchResource> batches
) {
}
