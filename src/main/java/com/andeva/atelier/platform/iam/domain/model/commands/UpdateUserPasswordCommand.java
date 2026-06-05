package com.andeva.atelier.platform.iam.domain.model.commands;

import java.util.UUID;

public record UpdateUserPasswordCommand(
        UUID userId,
        String currentPassword,
        String newPassword
) {
    public UpdateUserPasswordCommand {
        if (userId == null) throw new IllegalArgumentException("iam.error.userId.required");
        if (currentPassword == null || currentPassword.isBlank()) throw new IllegalArgumentException("iam.error.currentPassword.required");
        if (newPassword == null || newPassword.isBlank()) throw new IllegalArgumentException("iam.error.newPassword.required");
    }
}
