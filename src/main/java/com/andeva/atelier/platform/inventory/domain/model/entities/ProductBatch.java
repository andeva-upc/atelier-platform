package com.andeva.atelier.platform.inventory.domain.model.entities;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.InventoryQuantity;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import java.util.UUID;
import java.util.Date;
/**
 * Representa un lote de inventario físico.
 * No es un Aggregate Root, solo vive dentro de Product.
 */
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
    public UUID getBatchId() { return batchId; }
    public InventoryQuantity getAvailableQuantity() { return availableQuantity; }
    public Money getAcquisitionCost() { return acquisitionCost; }
    public void deductQuantity(InventoryQuantity amount) { this.availableQuantity = this.availableQuantity.subtract(amount); }
}
