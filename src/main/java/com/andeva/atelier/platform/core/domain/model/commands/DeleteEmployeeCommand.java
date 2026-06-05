package com.andeva.atelier.platform.core.domain.model.commands;

import java.util.UUID;

public record DeleteEmployeeCommand(UUID userId) {
    public DeleteEmployeeCommand {
        if (userId == null) throw new IllegalArgumentException("core.error.userId.required");
    }
}
