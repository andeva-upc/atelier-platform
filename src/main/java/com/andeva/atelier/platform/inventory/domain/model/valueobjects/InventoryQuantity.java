package com.andeva.atelier.platform.inventory.domain.model.valueobjects;
public record InventoryQuantity(int value) {
    public InventoryQuantity { if (value < 0) throw new IllegalArgumentException("Quantity cannot be negative"); }
    public InventoryQuantity add(InventoryQuantity other) { return new InventoryQuantity(this.value + other.value()); }
    public InventoryQuantity subtract(InventoryQuantity other) {
        if (this.value < other.value()) throw new IllegalArgumentException("Insufficient quantity");
        return new InventoryQuantity(this.value - other.value());
    }
}
