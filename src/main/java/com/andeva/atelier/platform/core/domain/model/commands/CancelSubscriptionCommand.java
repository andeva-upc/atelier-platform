package com.andeva.atelier.platform.core.domain.model.commands;

import java.util.UUID;

public record CancelSubscriptionCommand(
        UUID branchId
) {
    public CancelSubscriptionCommand {
        if (branchId == null) throw new IllegalArgumentException("core.error.branchId.required");
    }
}
