package com.andeva.atelier.platform.core.interfaces.rest.transform;

import com.andeva.atelier.platform.core.domain.model.commands.UpdateEmployeeCommand;
import com.andeva.atelier.platform.core.interfaces.rest.resources.UpdateEmployeeResource;

import java.util.UUID;

public class UpdateEmployeeCommandFromResourceAssembler {
    public static UpdateEmployeeCommand toCommandFromResource(UUID userId, UpdateEmployeeResource resource) {
        return new UpdateEmployeeCommand(
                userId,
                resource.firstName(),
                resource.lastName(),
                resource.documentType(),
                resource.documentNumber(),
                resource.phone()
        );
    }
}
