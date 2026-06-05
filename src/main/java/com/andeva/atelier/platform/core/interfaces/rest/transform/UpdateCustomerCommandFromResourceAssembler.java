package com.andeva.atelier.platform.core.interfaces.rest.transform;

import com.andeva.atelier.platform.core.domain.model.commands.UpdateCustomerCommand;
import com.andeva.atelier.platform.core.interfaces.rest.resources.UpdateCustomerResource;

import java.util.UUID;

public class UpdateCustomerCommandFromResourceAssembler {
    public static UpdateCustomerCommand toCommandFromResource(UUID userId, UpdateCustomerResource resource) {
        return new UpdateCustomerCommand(
                userId,
                resource.firstName(),
                resource.lastName(),
                resource.businessName(),
                resource.documentType(),
                resource.documentNumber(),
                resource.phone()
        );
    }
}
