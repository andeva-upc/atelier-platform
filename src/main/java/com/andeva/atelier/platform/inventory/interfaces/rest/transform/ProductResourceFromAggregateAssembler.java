package com.andeva.atelier.platform.inventory.interfaces.rest.transform;

import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.interfaces.rest.resources.ProductResource;

public class ProductResourceFromAggregateAssembler {
    public static ProductResource toResourceFromAggregate(Product aggregate) {
        return new ProductResource(
                aggregate.getId(),
                aggregate.getBranchId().value(),
                aggregate.getCategory().name(),
                aggregate.getName().name(),
                aggregate.getSku().value(),
                aggregate.getCurrentStock().value(),
                aggregate.getReservedStock().value()
        );
    }
}
