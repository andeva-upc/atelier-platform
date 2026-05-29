package com.andeva.atelier.platform.shared.domain.model.valueobjects;

import java.util.UUID;

public record VehicleId(UUID value) {

    private static final String NOT_NULL_UUID_REGEX = "shared.error.vehicleId.required";

    public VehicleId {
        if (value == null) {
            throw new IllegalArgumentException(NOT_NULL_UUID_REGEX);
        }
    }
}
