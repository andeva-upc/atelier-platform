package com.andeva.atelier.platform.core.domain.model.commands;

import com.andeva.atelier.platform.core.domain.model.valueobjects.OwnerId;

public record CreateWorkshopCommand(
        OwnerId ownerId,
        String businessName,
        String brandName,
        String taxId,
        int mileageIntervalConfig
) {
    public CreateWorkshopCommand {
        if (businessName == null || businessName.isBlank()) throw new IllegalArgumentException("core.error.businessName.required");
        if (brandName == null || brandName.isBlank()) throw new IllegalArgumentException("core.error.brandName.required");
        if (taxId == null || taxId.isBlank()) throw new IllegalArgumentException("core.error.taxId.required");
    }
}
