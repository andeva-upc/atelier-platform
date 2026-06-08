package com.andeva.atelier.platform.appointments.interfaces.rest.transform;

import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.appointments.interfaces.rest.resources.AppointmentResource;

public class AppointmentResourceFromAggregateAssembler {

    public static AppointmentResource toResourceFromAggregate(Appointment aggregate) {
        return new AppointmentResource(
                aggregate.getId(),
                aggregate.getBranchId().value(),
                aggregate.getCustomerId().value(),
                aggregate.getVehicleId().value(),
                aggregate.getScheduledStart(),
                aggregate.getScheduledEnd(),
                aggregate.getStatus().name(),
                aggregate.getNotes() == null ? null : aggregate.getNotes().value()
        );
    }
}