package com.andeva.atelier.platform.appointments.domain.model.commands;

import java.util.UUID;

public record DeleteAppointmentCommand(UUID appointmentId) {

    public DeleteAppointmentCommand {
        if (appointmentId == null) {
            throw new IllegalArgumentException("Appointment ID is required");
        }
    }
}