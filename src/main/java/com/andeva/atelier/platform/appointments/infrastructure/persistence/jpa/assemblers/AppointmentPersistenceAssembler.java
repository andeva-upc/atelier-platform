package com.andeva.atelier.platform.appointments.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.appointments.infrastructure.persistence.jpa.entities.AppointmentPersistenceEntity;

public class AppointmentPersistenceAssembler {

    public static AppointmentPersistenceEntity toEntityFromAggregate(Appointment aggregate) {
        var entity = new AppointmentPersistenceEntity();

        entity.setBranchId(aggregate.getBranchId());
        entity.setCustomerId(aggregate.getCustomerId());
        entity.setVehicleId(aggregate.getVehicleId());
        entity.setStatus(aggregate.getStatus());
        entity.setScheduledStart(aggregate.getScheduledStart());
        entity.setScheduledEnd(aggregate.getScheduledEnd());
        entity.setNotes(aggregate.getNotes());

        return entity;
    }

    public static Appointment toAggregateFromEntity(AppointmentPersistenceEntity entity) {
        return new Appointment(
                entity.getId(),
                entity.getBranchId(),
                entity.getCustomerId(),
                entity.getVehicleId(),
                entity.getScheduledStart(),
                entity.getScheduledEnd(),
                entity.getStatus(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt(),
                entity.getCreatedBy(),
                entity.getUpdatedBy(),
                entity.getVersion()
        );
    }
}