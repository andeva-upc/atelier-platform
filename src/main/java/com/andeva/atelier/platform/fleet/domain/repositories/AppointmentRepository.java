package com.andeva.atelier.platform.fleet.domain.repositories;

import com.andeva.atelier.platform.fleet.domain.model.aggregates.Appointment;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(UUID appointmentId);

    boolean existsById(UUID appointmentId);

    void deleteById(UUID appointmentId);

    boolean existsByScheduledStartLessThanAndScheduledEndGreaterThan(
            LocalDateTime scheduledEnd,
            LocalDateTime scheduledStart
    );

    boolean existsByIdNotAndScheduledStartLessThanAndScheduledEndGreaterThan(
            UUID appointmentId,
            LocalDateTime scheduledEnd,
            LocalDateTime scheduledStart
    );
}