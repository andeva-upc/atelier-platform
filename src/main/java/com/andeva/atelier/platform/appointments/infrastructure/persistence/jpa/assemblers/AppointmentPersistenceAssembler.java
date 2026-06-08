package com.andeva.atelier.platform.appointments.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.appointments.infrastructure.persistence.jpa.entities.AppointmentPersistenceEntity;

public class AppointmentPersistenceAssembler {

    public static AppointmentPersistenceEntity toEntityFromAggregate(Appointment aggregate) {
        var entity = new AppointmentPersistenceEntity();
        entity.setWorkshopId(aggregate.getWorkshopId());
        entity.setBranchId(aggregate.getBranchId());
        entity.setCustomerId(aggregate.getCustomerId());
        entity.setVehicleId(aggregate.getVehicleId());
        entity.setAppointmentDate(aggregate.getAppointmentDate());
        entity.setStatus(aggregate.getStatus());
        return entity;
    }

    public static Appointment toAggregateFromEntity(AppointmentPersistenceEntity entity) {
        return new Appointment(
                entity.getWorkshopId(),
                entity.getBranchId(),
                entity.getCustomerId(),
                entity.getVehicleId(),
                entity.getAppointmentDate(),
                entity.getStatus()
        );
    }
}