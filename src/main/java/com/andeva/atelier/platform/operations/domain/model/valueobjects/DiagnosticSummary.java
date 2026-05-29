package com.andeva.atelier.platform.operations.domain.model.valueobjects;

public record DiagnosticSummary(String value) {
    private static final int MAX_LENGTH = 2000;
    private static final String NOT_BLANK_MESSAGE_KEY = "operations.error.diagnosticSummary.notBlank";
    private static final String SIZE_MESSAGE_KEY = "operations.error.diagnosticSummary.tooLong";

    public DiagnosticSummary {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(NOT_BLANK_MESSAGE_KEY);
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(SIZE_MESSAGE_KEY);
        }
    }
}
