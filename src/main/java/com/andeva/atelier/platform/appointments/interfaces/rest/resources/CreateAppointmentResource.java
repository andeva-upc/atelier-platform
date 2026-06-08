package com.andeva.atelier.platform.appointments.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAppointmentResource(
        @NotNull(message = "Workshop ID is required") UUID workshopId,
        @NotNull(message = "Branch ID is required") UUID branchId,
        @NotNull(message = "Customer ID is required") UUID customerId,
        @NotNull(message = "Vehicle ID is required") UUID vehicleId,
        @NotNull(message = "Appointment date is required") LocalDateTime appointmentDate
) {
}