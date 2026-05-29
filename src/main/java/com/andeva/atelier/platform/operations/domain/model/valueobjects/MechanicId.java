package com.andeva.atelier.platform.operations.domain.model.valueobjects;

import java.util.UUID;

public record MechanicId(UUID value) {

    private static final String NOT_NULL_UUID_REGEX = "operations.error.mechanicId.required";

    public MechanicId {
        if (value == null) {
            throw new IllegalArgumentException(NOT_NULL_UUID_REGEX);
        }
    }
}
