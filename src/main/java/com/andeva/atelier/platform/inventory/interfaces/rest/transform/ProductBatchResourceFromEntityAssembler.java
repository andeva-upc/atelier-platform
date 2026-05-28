package com.andeva.atelier.platform.inventory.interfaces.rest.transform;
import com.andeva.atelier.platform.inventory.domain.model.entities.ProductBatch;
import com.andeva.atelier.platform.inventory.interfaces.rest.resources.ProductBatchResource;
public class ProductBatchResourceFromEntityAssembler {
    public static ProductBatchResource toResourceFromEntity(ProductBatch batch) {
        return new ProductBatchResource(batch.getBatchId(), batch.getAvailableQuantity().value(), batch.getAcquisitionCost().amount());
    }
}
