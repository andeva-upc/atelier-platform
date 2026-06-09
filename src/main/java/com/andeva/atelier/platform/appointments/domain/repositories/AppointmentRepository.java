package com.andeva.atelier.platform.appointments.domain.repositories;

import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;

import java.time.LocalDateTime;
import java.util.UUID;

public interface AppointmentRepository {

    Appointment save(Appointment appointment);

    boolean existsByScheduledStartLessThanAndScheduledEndGreaterThan(
            LocalDateTime scheduledEnd,
            LocalDateTime scheduledStart
    );

    boolean existsById(UUID appointmentId);

    void deleteById(UUID appointmentId);
}