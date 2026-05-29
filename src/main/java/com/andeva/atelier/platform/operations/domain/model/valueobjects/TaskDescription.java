package com.andeva.atelier.platform.operations.domain.model.valueobjects;

public record TaskDescription(String value) {

    private static final int MIN_LENGTH = 10;
    private static final int MAX_LENGTH = 1000;
    private static final String NOT_BLANK_KEY = "operations.error.taskDescription.required";
    private static final String TOO_SHORT_KEY = "operations.error.taskDescription.tooShort";
    private static final String TOO_LONG_KEY = "operations.error.taskDescription.tooLong";

    public TaskDescription {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(NOT_BLANK_KEY);
        }
        if (value.trim().length() < MIN_LENGTH) {
            throw new IllegalArgumentException(TOO_SHORT_KEY);
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(TOO_LONG_KEY);
        }
    }
}
