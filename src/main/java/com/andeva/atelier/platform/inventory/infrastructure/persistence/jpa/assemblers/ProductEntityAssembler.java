package com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.entities.ProductBatch;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.*;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.entities.ProductBatchJpaEntity;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.entities.ProductJpaEntity;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;

import java.util.stream.Collectors;

public class ProductEntityAssembler {
    public static ProductJpaEntity toEntity(Product product) {
        if (product == null) return null;
        ProductJpaEntity entity = new ProductJpaEntity();
        
        if (product.getVersion() != null) {
            entity.setId(product.getId());
            entity.setVersion(product.getVersion());
        }
        
        entity.setBranchId(product.getBranchId());
        entity.setCategory(product.getCategory().value());
        entity.setName(product.getName().name());
        entity.setSku(product.getSku().value());
        entity.setDescription(product.getDescription());
        entity.setCurrentSellingPrice(product.getCurrentSellingPrice());
        entity.setCurrentStock(product.getCurrentStock().value());
        entity.setMinimumStock(product.getMinimumStock());

        var batchEntities = product.getBatches().stream().map(b -> {
            ProductBatchJpaEntity be = new ProductBatchJpaEntity();
            if (b.getVersion() != null) {
                be.setId(b.getBatchId());
                be.setVersion(b.getVersion());
            }
            be.setBranchId(product.getBranchId());
            be.setInitialQuantity(b.getInitialQuantity().value()); 
            be.setAvailableQuantity(b.getAvailableQuantity().value());
            be.setAcquisitionCost(b.getAcquisitionCost());
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
                batches.add(ProductBatch.reconstitute(
                    be.getId(), 
                    new InventoryQuantity(be.getInitialQuantity()), 
                    new InventoryQuantity(be.getAvailableQuantity()), 
                    be.getAcquisitionCost(),
                    be.getVersion()
                ));
            }
        }
        return Product.reconstitute(
            entity.getId(), 
            entity.getBranchId(), 
            new ProductCategory(entity.getCategory()), 
            new ProductName(entity.getName()), 
            new Sku(entity.getSku()), 
            new InventoryQuantity(entity.getCurrentStock()), 
            entity.getCurrentSellingPrice(),
            entity.getDescription(),
            entity.getMinimumStock(),
            entity.getVersion(),
            batches
        );
    }
}
