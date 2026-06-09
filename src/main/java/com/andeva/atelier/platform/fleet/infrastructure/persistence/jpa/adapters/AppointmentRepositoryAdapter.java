package com.andeva.atelier.platform.fleet.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.fleet.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.fleet.domain.repositories.AppointmentRepository;
import com.andeva.atelier.platform.fleet.infrastructure.persistence.jpa.assemblers.AppointmentPersistenceAssembler;
import com.andeva.atelier.platform.fleet.infrastructure.persistence.jpa.repositories.AppointmentJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

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
    @Override
    public boolean existsById(UUID appointmentId) {
        return appointmentJpaRepository.existsById(appointmentId);
    }

    @Override
    public void deleteById(UUID appointmentId) {
        appointmentJpaRepository.findById(appointmentId)
                .ifPresent(appointmentJpaRepository::delete);
    }
    @Override
    public Optional<Appointment> findById(UUID appointmentId) {
        return appointmentJpaRepository.findById(appointmentId)
                .map(AppointmentPersistenceAssembler::toAggregateFromEntity);
    }

    @Override
    public boolean existsByIdNotAndScheduledStartLessThanAndScheduledEndGreaterThan(
            UUID appointmentId,
            LocalDateTime scheduledEnd,
            LocalDateTime scheduledStart
    ) {
        return appointmentJpaRepository.existsByIdNotAndScheduledStartLessThanAndScheduledEndGreaterThan(
                appointmentId,
                scheduledEnd,
                scheduledStart
        );
    }
}