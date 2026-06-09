package com.andeva.atelier.platform.fleet.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateAppointmentResource(
        @NotNull(message = "Branch ID is required") UUID branchId,
        @NotNull(message = "Customer ID is required") UUID customerId,
        @NotNull(message = "Vehicle ID is required") UUID vehicleId,
        @NotNull(message = "Scheduled start is required") LocalDateTime scheduledStart,
        String notes
) {
}