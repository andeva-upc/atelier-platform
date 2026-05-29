package com.andeva.atelier.platform.shared.domain.model.valueobjects;

public record Mileage(Integer value) {
    private static final String NOT_NULL_MESSAGE_KEY = "operations.error.mileage.required";
    private static final String NOT_NEGATIVE_MESSAGE_KEY = "operations.error.mileage.cannotBeNegative";
    public Mileage {
        if (value == null) {
            throw new IllegalArgumentException(NOT_NULL_MESSAGE_KEY);
        }
        if (value < 0) {
            throw new IllegalArgumentException(NOT_NEGATIVE_MESSAGE_KEY);
        }
    }
}
