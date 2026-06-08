package com.andeva.atelier.platform.iot.application.commandservices;

/**
 * Interface representing possible failure outcomes of Telemetry command operations.
 */
public sealed interface TelemetryCommandFailure permits
        TelemetryCommandFailure.NotFound,
        TelemetryCommandFailure.InvalidState {
    record NotFound(String message) implements TelemetryCommandFailure {}
    record InvalidState(String message) implements TelemetryCommandFailure {}
}
