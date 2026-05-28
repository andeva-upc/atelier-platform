package com.andeva.atelier.platform.inventory.interfaces.rest.transform;
import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.interfaces.rest.resources.ProductResource;
public class ProductResourceFromAggregateAssembler {
    public static ProductResource toResourceFromAggregate(Product product) {
        return new ProductResource(product.getId(), product.getBranchId().value(), product.getCategory().name(), product.getName().name(), product.getSku().value(), product.getCurrentStock().value(), product.getReservedStock().value());
    }
}
