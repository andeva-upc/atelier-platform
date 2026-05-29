package com.andeva.atelier.platform.operations.domain.model.valueobjects;

public record Quantity(Integer value) {
    private static final String NOT_NULL_MESSAGE_KEY = "operations.error.quantity.required";
    private static final String STRICTLY_POSITIVE_MESSAGE_KEY = "operations.error.quantity.mustBeGreaterThanZero";

    public Quantity {
        if (value == null) {
            throw new IllegalArgumentException(NOT_NULL_MESSAGE_KEY);
        }
        if (value <= 0) {
            throw new IllegalArgumentException(STRICTLY_POSITIVE_MESSAGE_KEY);
        }
    }
}
