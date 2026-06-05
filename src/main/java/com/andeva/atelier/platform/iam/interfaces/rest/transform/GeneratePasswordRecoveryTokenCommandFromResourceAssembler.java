package com.andeva.atelier.platform.iam.interfaces.rest.transform;

import com.andeva.atelier.platform.iam.domain.model.commands.GeneratePasswordRecoveryTokenCommand;
import com.andeva.atelier.platform.iam.interfaces.rest.resources.PasswordRecoveryResource;

public class GeneratePasswordRecoveryTokenCommandFromResourceAssembler {
    public static GeneratePasswordRecoveryTokenCommand toCommandFromResource(PasswordRecoveryResource resource) {
        return new GeneratePasswordRecoveryTokenCommand(resource.email());
    }
}
