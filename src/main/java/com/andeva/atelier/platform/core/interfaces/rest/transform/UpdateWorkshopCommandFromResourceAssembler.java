package com.andeva.atelier.platform.core.interfaces.rest.transform;

import com.andeva.atelier.platform.core.domain.model.commands.UpdateWorkshopCommand;
import com.andeva.atelier.platform.core.interfaces.rest.resources.UpdateWorkshopResource;

import java.util.UUID;

public class UpdateWorkshopCommandFromResourceAssembler {
    public static UpdateWorkshopCommand toCommandFromResource(UUID id, UpdateWorkshopResource resource) {
        return new UpdateWorkshopCommand(
                id,
                resource.businessName(),
                resource.brandName(),
                resource.taxId(),
                resource.mileageIntervalConfig()
        );
    }
}
