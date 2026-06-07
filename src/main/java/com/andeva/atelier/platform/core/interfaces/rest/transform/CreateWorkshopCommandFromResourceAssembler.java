package com.andeva.atelier.platform.core.interfaces.rest.transform;

import com.andeva.atelier.platform.core.domain.model.commands.CreateWorkshopCommand;
import com.andeva.atelier.platform.core.domain.model.valueobjects.OwnerId;
import com.andeva.atelier.platform.core.interfaces.rest.resources.CreateWorkshopResource;

public class CreateWorkshopCommandFromResourceAssembler {
    public static CreateWorkshopCommand toCommandFromResource(CreateWorkshopResource resource) {
        return new CreateWorkshopCommand(
                new OwnerId(resource.ownerId()),
                resource.businessName(),
                resource.brandName(),
                resource.taxId(),
                resource.mileageIntervalConfig()
        );
    }
}
