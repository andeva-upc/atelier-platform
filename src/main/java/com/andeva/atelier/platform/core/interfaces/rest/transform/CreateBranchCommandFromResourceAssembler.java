package com.andeva.atelier.platform.core.interfaces.rest.transform;

import com.andeva.atelier.platform.core.domain.model.commands.CreateBranchCommand;
import com.andeva.atelier.platform.core.interfaces.rest.resources.CreateBranchResource;

public class CreateBranchCommandFromResourceAssembler {
    public static CreateBranchCommand toCommandFromResource(CreateBranchResource resource) {
        return new CreateBranchCommand(
                resource.workshopId(),
                resource.code(),
                resource.name(),
                resource.address(),
                resource.phone()
        );
    }
}
