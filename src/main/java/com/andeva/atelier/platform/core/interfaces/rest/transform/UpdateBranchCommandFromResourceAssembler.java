package com.andeva.atelier.platform.core.interfaces.rest.transform;

import com.andeva.atelier.platform.core.domain.model.commands.UpdateBranchCommand;
import com.andeva.atelier.platform.core.interfaces.rest.resources.UpdateBranchResource;

import java.util.UUID;

public class UpdateBranchCommandFromResourceAssembler {
    public static UpdateBranchCommand toCommandFromResource(UUID id, UpdateBranchResource resource) {
        return new UpdateBranchCommand(
                id,
                resource.code(),
                resource.name(),
                resource.address(),
                resource.phone()
        );
    }
}
