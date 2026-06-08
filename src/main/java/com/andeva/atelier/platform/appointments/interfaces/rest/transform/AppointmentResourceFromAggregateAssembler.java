package com.andeva.atelier.platform.appointments.interfaces.rest.transform;

import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.appointments.interfaces.rest.resources.AppointmentResource;

public class AppointmentResourceFromAggregateAssembler {
    public static AppointmentResource toResourceFromAggregate(Appointment aggregate) {
        return new AppointmentResource(
                aggregate.getWorkshopId(),
                aggregate.getBranchId(),
                aggregate.getCustomerId(),
                aggregate.getVehicleId(),
                aggregate.getAppointmentDate(),
                aggregate.getStatus()
        );
    }
}