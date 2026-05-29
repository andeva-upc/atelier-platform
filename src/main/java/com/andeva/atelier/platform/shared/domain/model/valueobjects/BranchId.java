package com.andeva.atelier.platform.shared.domain.model.valueobjects;

import java.util.UUID;

public record BranchId(UUID value) {

    private static final String NOT_NULL_UUID_REGEX = "shared.error.branchId.required";

    public BranchId {
        if (value == null) {
            throw new IllegalArgumentException(NOT_NULL_UUID_REGEX);
        }
    }
}
