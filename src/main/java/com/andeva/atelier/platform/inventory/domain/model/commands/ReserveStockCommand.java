package com.andeva.atelier.platform.inventory.domain.model.commands;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.InventoryQuantity;
import java.util.UUID;
public record ReserveStockCommand(UUID productId, InventoryQuantity quantity) {}
