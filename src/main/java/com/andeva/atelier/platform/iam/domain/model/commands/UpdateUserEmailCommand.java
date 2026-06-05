package com.andeva.atelier.platform.iam.domain.model.commands;

import java.util.UUID;

public record UpdateUserEmailCommand(
        UUID userId,
        String newEmail
) {
    public UpdateUserEmailCommand {
        if (userId == null) throw new IllegalArgumentException("iam.error.userId.required");
        if (newEmail == null || newEmail.isBlank()) throw new IllegalArgumentException("iam.error.email.required");
    }
}
