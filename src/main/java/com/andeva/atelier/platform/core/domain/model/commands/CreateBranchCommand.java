package com.andeva.atelier.platform.core.domain.model.commands;

import java.util.UUID;

public record CreateBranchCommand(
        UUID workshopId,
        String code,
        String name,
        String address,
        String phone
) {
    public CreateBranchCommand {
        if (workshopId == null) throw new IllegalArgumentException("core.error.workshopId.required");
        if (code == null || code.isBlank()) throw new IllegalArgumentException("core.error.code.required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("core.error.name.required");
    }
}
