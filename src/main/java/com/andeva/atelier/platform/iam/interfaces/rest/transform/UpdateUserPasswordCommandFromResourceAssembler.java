package com.andeva.atelier.platform.iam.interfaces.rest.transform;

import com.andeva.atelier.platform.iam.domain.model.commands.UpdateUserPasswordCommand;
import com.andeva.atelier.platform.iam.interfaces.rest.resources.UpdateUserPasswordResource;

import java.util.UUID;

public class UpdateUserPasswordCommandFromResourceAssembler {
    public static UpdateUserPasswordCommand toCommandFromResource(UUID userId, UpdateUserPasswordResource resource) {
        return new UpdateUserPasswordCommand(
                userId,
                resource.currentPassword(),
                resource.newPassword()
        );
    }
}
