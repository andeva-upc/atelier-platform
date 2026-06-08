package com.andeva.atelier.platform.inventory.interfaces.rest.resources;

public record ProductBatchResource(
        String batchId,
        Integer initialQuantity,
        Integer availableQuantity,
        Double acquisitionCost
) {
}
