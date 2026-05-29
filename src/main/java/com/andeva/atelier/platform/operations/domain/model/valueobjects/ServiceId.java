package com.andeva.atelier.platform.operations.domain.model.valueobjects;

import java.util.UUID;

public record ServiceId(UUID value) {

    private static final String NOT_NULL_UUID_REGEX = "operations.error.serviceId.required";

    public ServiceId {
        if (value == null) {
            throw new IllegalArgumentException(NOT_NULL_UUID_REGEX);
        }
    }
}
