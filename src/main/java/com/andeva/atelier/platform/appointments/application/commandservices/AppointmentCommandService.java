package com.andeva.atelier.platform.appointments.application.commandservices;

import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.appointments.domain.model.commands.CreateAppointmentCommand;
import com.andeva.atelier.platform.shared.application.result.Result;

public interface AppointmentCommandService {
    Result<Appointment, AppointmentCommandFailure> handle(CreateAppointmentCommand command);
}