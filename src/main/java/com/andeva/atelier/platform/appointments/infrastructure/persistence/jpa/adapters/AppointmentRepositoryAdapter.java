package com.andeva.atelier.platform.appointments.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.appointments.domain.repositories.AppointmentRepository;
import com.andeva.atelier.platform.appointments.infrastructure.persistence.jpa.assemblers.AppointmentPersistenceAssembler;
import com.andeva.atelier.platform.appointments.infrastructure.persistence.jpa.repositories.AppointmentJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class AppointmentRepositoryAdapter implements AppointmentRepository {

    private final AppointmentJpaRepository appointmentJpaRepository;

    public AppointmentRepositoryAdapter(AppointmentJpaRepository appointmentJpaRepository) {
        this.appointmentJpaRepository = appointmentJpaRepository;
    }

    @Override
    public Appointment save(Appointment appointment) {
        var entity = AppointmentPersistenceAssembler.toEntityFromAggregate(appointment);
        var savedEntity = appointmentJpaRepository.save(entity);
        return AppointmentPersistenceAssembler.toAggregateFromEntity(savedEntity);
    }

    @Override
    public boolean existsByScheduledStartLessThanAndScheduledEndGreaterThan(
            LocalDateTime scheduledEnd,
            LocalDateTime scheduledStart
    ) {
        return appointmentJpaRepository.existsByScheduledStartLessThanAndScheduledEndGreaterThan(
                scheduledEnd,
                scheduledStart
        );
    }
}