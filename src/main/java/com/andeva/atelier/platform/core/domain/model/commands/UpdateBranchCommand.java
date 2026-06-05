package com.andeva.atelier.platform.core.domain.model.commands;

import java.util.UUID;

public record UpdateBranchCommand(
        UUID id,
        String code,
        String name,
        String address,
        String phone
) {
    public UpdateBranchCommand {
        if (id == null) throw new IllegalArgumentException("core.error.branchId.required");
        if (code == null || code.isBlank()) throw new IllegalArgumentException("core.error.code.required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("core.error.name.required");
    }
}
