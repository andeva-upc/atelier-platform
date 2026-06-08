package com.andeva.atelier.platform.inventory.domain.model.valueobjects;

public record InventoryQuantity(Integer value) {
    public InventoryQuantity {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Quantity cannot be null or negative");
        }
    }

    public InventoryQuantity add(InventoryQuantity quantity) {
        return new InventoryQuantity(this.value + quantity.value());
    }

    public InventoryQuantity subtract(InventoryQuantity quantity) {
        if (this.value < quantity.value()) {
            throw new IllegalArgumentException("Resulting quantity cannot be negative");
        }
        return new InventoryQuantity(this.value - quantity.value());
    }
}
