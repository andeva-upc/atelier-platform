package com.andeva.atelier.platform.inventory.interfaces.rest.transform;

import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.interfaces.rest.resources.ProductDetailsResource;

public class ProductDetailsResourceFromAggregateAssembler {
    public static ProductDetailsResource toResourceFromAggregate(Product aggregate) {
        var batches = aggregate.getBatches().stream()
                .map(ProductBatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return new ProductDetailsResource(
                aggregate.getId().toString(),
                aggregate.getBranchId().value().toString(),
                aggregate.getCategory().name(),
                aggregate.getName().name(),
                aggregate.getSku().value(),
                aggregate.getDescription(),
                aggregate.getSalePrice().amount().doubleValue(),
                aggregate.getCurrentStock().value(),
                aggregate.getReservedStock().value(),
                aggregate.getMinimumStock().value(),
                batches
        );
    }
}
