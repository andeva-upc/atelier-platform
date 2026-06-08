package com.andeva.atelier.platform.inventory.domain.model.aggregates;

import com.andeva.atelier.platform.inventory.domain.exceptions.InsufficientStockException;
import com.andeva.atelier.platform.inventory.domain.model.entities.ProductBatch;
import com.andeva.atelier.platform.inventory.domain.model.events.ProductCreatedEvent;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.*;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Product extends AbstractAggregateRoot<Product> {
    private final UUID id;
    private final BranchId branchId;
    private ProductCategory category;
    private ProductName name;
    private Sku sku;
    private InventoryQuantity currentStock;
    private InventoryQuantity reservedStock;
    private final List<ProductBatch> batches;

    public Product(UUID id, BranchId branchId, ProductCategory category, ProductName name, Sku sku) {
        this.id = id != null ? id : UUID.randomUUID();
        this.branchId = branchId;
        this.category = category;
        this.name = name;
        this.sku = sku;
        this.currentStock = new InventoryQuantity(0);
        this.reservedStock = new InventoryQuantity(0);
        this.batches = new ArrayList<>();
        this.registerEvent(new ProductCreatedEvent(this, this.branchId, this.id));
    }

    private Product(UUID id, BranchId branchId, ProductCategory category, ProductName name, Sku sku, InventoryQuantity currentStock, InventoryQuantity reservedStock) {
        this.id = id;
        this.branchId = branchId;
        this.category = category;
        this.name = name;
        this.sku = sku;
        this.currentStock = currentStock;
        this.reservedStock = reservedStock;
        this.batches = new ArrayList<>();
    }

    public static Product reconstitute(UUID id, BranchId branchId, ProductCategory category, ProductName name, Sku sku, InventoryQuantity currentStock, InventoryQuantity reservedStock, List<ProductBatch> batches) {
        Product p = new Product(id, branchId, category, name, sku, currentStock, reservedStock);
        if (batches != null) {
            p.batches.addAll(batches);
        }
        return p;
    }

    public UUID getId() { return id; }
    public BranchId getBranchId() { return branchId; }
    public ProductCategory getCategory() { return category; }
    public ProductName getName() { return name; }
    public Sku getSku() { return sku; }
    public InventoryQuantity getCurrentStock() { return currentStock; }
    public InventoryQuantity getReservedStock() { return reservedStock; }
    public List<ProductBatch> getBatches() { return Collections.unmodifiableList(batches); }

    public void addBatch(ProductBatch batch) {
        this.batches.add(batch);
        this.currentStock = this.currentStock.add(batch.getAvailableQuantity());
    }

    public void updateDetails(ProductName name, ProductCategory category, Sku sku) {
        this.name = name;
        this.category = category;
        this.sku = sku;
    }

    public void reserveStock(InventoryQuantity amount) {
        if (this.currentStock.value() < amount.value()) throw new InsufficientStockException("Not enough stock available");
        this.currentStock = this.currentStock.subtract(amount);
        this.reservedStock = this.reservedStock.add(amount);
    }

    public void releaseStock(InventoryQuantity amount) {
        this.reservedStock = this.reservedStock.subtract(amount);
        this.currentStock = this.currentStock.add(amount);
    }

    public void dispatchStock(InventoryQuantity amount) {
        this.reservedStock = this.reservedStock.subtract(amount);
        int remainingToDeduct = amount.value();
        for (ProductBatch batch : this.batches) {
            if (remainingToDeduct <= 0) break;
            int batchAvail = batch.getAvailableQuantity().value();
            if (batchAvail > 0) {
                int deduct = Math.min(batchAvail, remainingToDeduct);
                batch.deductQuantity(new InventoryQuantity(deduct));
                remainingToDeduct -= deduct;
            }
        }
    }
}
