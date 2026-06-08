package com.andeva.atelier.platform.appointments.domain.model.commands;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateAppointmentCommand(
        UUID workshopId,
        UUID branchId,
        UUID customerId,
        UUID vehicleId,
        LocalDateTime appointmentDate
) {
    public CreateAppointmentCommand {
        if (workshopId == null) throw new IllegalArgumentException("Workshop ID cannot be null");
        if (branchId == null) throw new IllegalArgumentException("Branch ID cannot be null");
        if (customerId == null) throw new IllegalArgumentException("Customer ID cannot be null");
        if (vehicleId == null) throw new IllegalArgumentException("Vehicle ID cannot be null");
        if (appointmentDate == null) throw new IllegalArgumentException("Appointment date cannot be null");
    }
}