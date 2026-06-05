package com.andeva.atelier.platform.core.domain.model.commands;

import java.util.UUID;

public record DeleteOwnerCommand(UUID userId) {
    public DeleteOwnerCommand {
        if (userId == null) throw new IllegalArgumentException("core.error.userId.required");
    }
}
