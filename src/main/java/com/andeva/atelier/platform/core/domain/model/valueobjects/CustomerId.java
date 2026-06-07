package com.andeva.atelier.platform.core.domain.model.valueobjects;

import java.util.UUID;

public record CustomerId(UUID value) {
    public CustomerId {
        if (value == null) {
            throw new IllegalArgumentException("core.error.customerId.required");
        }
    }
}
