package com.andeva.atelier.platform.core.domain.model.commands;

import java.util.UUID;

public record DeleteCustomerCommand(UUID userId) {
    public DeleteCustomerCommand {
        if (userId == null) throw new IllegalArgumentException("core.error.userId.required");
    }
}
