package com.andeva.atelier.platform.appointments.domain.model.valueobjects;

public record AppointmentsSummary(String value) {
    private static final int MAX_LENGTH = 2000;
    private static final String NOT_BLANK_MESSAGE_KEY = "fleet.error.appointmentsSummary.notBlank";
    private static final String SIZE_MESSAGE_KEY = "fleet.error.appointmentsSummary.tooLong";

    /**
     * Constructor for AppointmentsSummary that performs validation on the input value.
     * @param value the appointments summary string to be validated and assigned to the record. It must not be null, blank, and must not exceed the maximum length defined by MAX_LENGTH.
     * @throws IllegalArgumentException if the value is null, blank, or exceeds the maximum length, with appropriate error messages defined in the constants.
     */
    public AppointmentsSummary {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(NOT_BLANK_MESSAGE_KEY);
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(SIZE_MESSAGE_KEY);
        }
    }
}
