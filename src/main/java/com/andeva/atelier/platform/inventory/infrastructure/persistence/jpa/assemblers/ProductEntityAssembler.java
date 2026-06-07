package com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.entities.ProductBatch;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.*;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.entities.ProductBatchJpaEntity;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.entities.ProductJpaEntity;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;

import java.util.stream.Collectors;

public class ProductEntityAssembler {
    public static ProductJpaEntity toEntity(Product product) {
        if (product == null) return null;
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.setId(product.getId());
        entity.setBranchId(product.getBranchId().value());
        entity.setCategory(product.getCategory().name());
        entity.setName(product.getName().name());
        entity.setSku(product.getSku().value());
        entity.setCurrentStock(product.getCurrentStock().value());
        entity.setReservedStock(product.getReservedStock().value());

        var batchEntities = product.getBatches().stream().map(b -> {
            ProductBatchJpaEntity be = new ProductBatchJpaEntity();
            be.setBatchId(b.getBatchId());
            be.setInitialQuantity(b.getInitialQuantity().value()); 
            be.setAvailableQuantity(b.getAvailableQuantity().value());
            be.setAcquisitionCost(b.getAcquisitionCost().amount());
            be.setProduct(entity);
            return be;
        }).collect(Collectors.toList());

        entity.setBatches(batchEntities);
        return entity;
    }

    public static Product toAggregate(ProductJpaEntity entity) {
        if (entity == null) return null;
        java.util.List<ProductBatch> batches = new java.util.ArrayList<>();
        if (entity.getBatches() != null) {
            for (ProductBatchJpaEntity be : entity.getBatches()) {
                batches.add(ProductBatch.reconstitute(be.getBatchId(), new InventoryQuantity(be.getInitialQuantity()), new InventoryQuantity(be.getAvailableQuantity()), new Money(be.getAcquisitionCost())));
            }
        }
        return Product.reconstitute(entity.getId(), new BranchId(entity.getBranchId()), ProductCategory.valueOf(entity.getCategory()), new ProductName(entity.getName()), new Sku(entity.getSku()), new InventoryQuantity(entity.getCurrentStock()), new InventoryQuantity(entity.getReservedStock()), batches);
    }
}
