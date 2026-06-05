package com.andeva.atelier.platform.core.domain.model.commands;

import java.util.UUID;

public record UpdateWorkshopCommand(
        UUID id,
        String businessName,
        String brandName,
        String taxId,
        int mileageIntervalConfig
) {
    public UpdateWorkshopCommand {
        if (id == null) throw new IllegalArgumentException("core.error.workshopId.required");
        if (businessName == null || businessName.isBlank()) throw new IllegalArgumentException("core.error.businessName.required");
        if (brandName == null || brandName.isBlank()) throw new IllegalArgumentException("core.error.brandName.required");
        if (taxId == null || taxId.isBlank()) throw new IllegalArgumentException("core.error.taxId.required");
    }
}
