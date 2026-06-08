package com.andeva.atelier.platform.inventory.domain.model.entities;

import com.andeva.atelier.platform.inventory.domain.model.valueobjects.InventoryQuantity;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;

import java.util.Date;
import java.util.UUID;

public class ProductBatch {
    private final UUID batchId;
    private final InventoryQuantity initialQuantity;
    private InventoryQuantity availableQuantity;
    private final Money acquisitionCost;
    private final Date receptionDate;

    public ProductBatch(UUID batchId, InventoryQuantity initialQuantity, Money acquisitionCost) {
        this.batchId = batchId != null ? batchId : UUID.randomUUID();
        this.initialQuantity = initialQuantity;
        this.availableQuantity = initialQuantity;
        this.acquisitionCost = acquisitionCost;
        this.receptionDate = new Date();
    }

    private ProductBatch(UUID batchId, InventoryQuantity initialQuantity, InventoryQuantity availableQuantity, Money acquisitionCost) {
        this.batchId = batchId;
        this.initialQuantity = initialQuantity;
        this.availableQuantity = availableQuantity;
        this.acquisitionCost = acquisitionCost;
        this.receptionDate = new Date();
    }

    public static ProductBatch reconstitute(UUID batchId, InventoryQuantity initialQuantity, InventoryQuantity availableQuantity, Money acquisitionCost) {
        return new ProductBatch(batchId, initialQuantity, availableQuantity, acquisitionCost);
    }

    public UUID getBatchId() { return batchId; }
    public InventoryQuantity getInitialQuantity() { return initialQuantity; }
    public InventoryQuantity getAvailableQuantity() { return availableQuantity; }
    public Money getAcquisitionCost() { return acquisitionCost; }
    public void deductQuantity(InventoryQuantity amount) { this.availableQuantity = this.availableQuantity.subtract(amount); }
}
