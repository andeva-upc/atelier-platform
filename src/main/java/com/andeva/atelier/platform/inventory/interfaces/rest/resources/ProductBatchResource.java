package com.andeva.atelier.platform.inventory.interfaces.rest.resources;
import java.util.UUID;
public record ProductBatchResource(UUID batchId, Integer availableQuantity, Double acquisitionCost) {}
