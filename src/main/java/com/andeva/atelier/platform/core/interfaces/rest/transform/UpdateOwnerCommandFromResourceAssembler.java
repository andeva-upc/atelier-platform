package com.andeva.atelier.platform.core.interfaces.rest.transform;

import com.andeva.atelier.platform.core.domain.model.commands.UpdateOwnerCommand;
import com.andeva.atelier.platform.core.interfaces.rest.resources.UpdateOwnerResource;

import java.util.UUID;

public class UpdateOwnerCommandFromResourceAssembler {
    public static UpdateOwnerCommand toCommandFromResource(UUID userId, UpdateOwnerResource resource) {
        return new UpdateOwnerCommand(
                userId,
                resource.firstName(),
                resource.lastName(),
                resource.documentType(),
                resource.documentNumber(),
                resource.phone()
        );
    }
}
