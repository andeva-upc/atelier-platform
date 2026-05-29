package com.andeva.atelier.platform.operations.domain.model.valueobjects;

import java.util.UUID;

public record ProductId(UUID value) {

    private static final String NOT_NULL_UUID_REGEX = "operations.error.productId.required";

    public ProductId {
        if (value == null) {
            throw new IllegalArgumentException(NOT_NULL_UUID_REGEX);
        }
    }
}
