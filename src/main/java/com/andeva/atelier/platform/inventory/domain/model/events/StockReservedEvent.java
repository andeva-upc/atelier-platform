package com.andeva.atelier.platform.inventory.domain.model.events;
import java.util.UUID;
public record StockReservedEvent(Object source, UUID productId, int quantity) {}
