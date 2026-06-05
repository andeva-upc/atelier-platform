package com.andeva.atelier.platform.core.interfaces.rest.transform;

import com.andeva.atelier.platform.core.domain.model.commands.CreateCustomerCommand;
import com.andeva.atelier.platform.core.interfaces.rest.resources.CreateCustomerResource;

public class CreateCustomerCommandFromResourceAssembler {
    public static CreateCustomerCommand toCommandFromResource(CreateCustomerResource resource) {
        return new CreateCustomerCommand(
                resource.userId(),
                resource.isCorporate(),
                resource.firstName(),
                resource.lastName(),
                resource.businessName(),
                resource.documentType(),
                resource.documentNumber(),
                resource.phone()
        );
    }
}
