package com.andeva.atelier.platform.core.domain.model.valueobjects;

import java.util.UUID;

public record BranchId(UUID value) {
    public BranchId {
        if (value == null) {
            throw new IllegalArgumentException("core.error.branchId.required");
        }
    }
}
