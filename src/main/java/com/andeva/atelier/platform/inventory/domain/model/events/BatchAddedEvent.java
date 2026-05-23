package com.andeva.atelier.platform.inventory.domain.model.events;
import java.util.UUID;
public record BatchAddedEvent(Object source, UUID productId, UUID batchId) {}
