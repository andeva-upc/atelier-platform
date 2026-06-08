package com.andeva.atelier.platform.appointments.application.internal.commandservices;

import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandFailure;
import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandService;
import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.appointments.domain.model.commands.CreateAppointmentCommand;
import com.andeva.atelier.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {

    @Override
    public Result<Appointment, AppointmentCommandFailure> handle(CreateAppointmentCommand command) {
        try {
            var appointment = new Appointment(command);
            return Result.success(appointment);
        } catch (IllegalArgumentException exception) {
            return Result.failure(AppointmentCommandFailure.INVALID_APPOINTMENT_DATA);
        }
    }
}