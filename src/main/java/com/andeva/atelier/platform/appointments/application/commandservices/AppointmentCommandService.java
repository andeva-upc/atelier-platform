package com.andeva.atelier.platform.appointments.application.commandservices;

import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.appointments.domain.model.commands.CreateAppointmentCommand;
import com.andeva.atelier.platform.appointments.domain.model.commands.DeleteAppointmentCommand;
import com.andeva.atelier.platform.shared.application.result.Result;

import java.util.UUID;

public interface AppointmentCommandService {

    Result<Appointment, AppointmentCommandFailure> handle(CreateAppointmentCommand command);

    Result<UUID, AppointmentCommandFailure> handle(DeleteAppointmentCommand command);
}