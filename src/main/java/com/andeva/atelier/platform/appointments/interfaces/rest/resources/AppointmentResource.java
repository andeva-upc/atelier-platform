package com.andeva.atelier.platform.appointments.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.UUID;

public record AppointmentResource(
        UUID workshopId,
        UUID branchId,
        UUID customerId,
        UUID vehicleId,
        LocalDateTime appointmentDate,
        String status
) {
}