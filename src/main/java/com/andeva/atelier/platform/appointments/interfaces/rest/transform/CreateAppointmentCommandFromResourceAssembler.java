package com.andeva.atelier.platform.appointments.interfaces.rest.transform;

import com.andeva.atelier.platform.appointments.domain.model.commands.CreateAppointmentCommand;
import com.andeva.atelier.platform.appointments.interfaces.rest.resources.CreateAppointmentResource;

public class CreateAppointmentCommandFromResourceAssembler {
    public static CreateAppointmentCommand toCommandFromResource(CreateAppointmentResource resource) {
        return new CreateAppointmentCommand(
                resource.workshopId(),
                resource.branchId(),
                resource.customerId(),
                resource.vehicleId(),
                resource.appointmentDate()
        );
    }
}