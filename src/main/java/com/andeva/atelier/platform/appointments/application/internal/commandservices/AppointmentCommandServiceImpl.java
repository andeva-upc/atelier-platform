package com.andeva.atelier.platform.appointments.application.internal.commandservices;

import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandFailure;
import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandService;
import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.appointments.domain.model.commands.CreateAppointmentCommand;
import com.andeva.atelier.platform.appointments.domain.repositories.AppointmentRepository;
import com.andeva.atelier.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentCommandServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Result<Appointment, AppointmentCommandFailure> handle(CreateAppointmentCommand command) {
        try {
            var appointment = new Appointment(command);
            var savedAppointment = appointmentRepository.save(appointment);
            return Result.success(savedAppointment);
        } catch (IllegalArgumentException exception) {
            return Result.failure(AppointmentCommandFailure.INVALID_APPOINTMENT_DATA);
        }
    }
}