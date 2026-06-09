package com.andeva.atelier.platform.appointments.application.internal.commandservices;

import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandFailure;
import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandService;
import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.appointments.domain.model.commands.CreateAppointmentCommand;
import com.andeva.atelier.platform.appointments.domain.model.commands.DeleteAppointmentCommand;
import com.andeva.atelier.platform.appointments.domain.repositories.AppointmentRepository;
import com.andeva.atelier.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AppointmentCommandServiceImpl implements AppointmentCommandService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentCommandServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional
    public Result<Appointment, AppointmentCommandFailure> handle(CreateAppointmentCommand command) {
        try {
            var scheduledStart = command.scheduledStart();
            var scheduledEnd = scheduledStart.plusHours(1);

            boolean overlap = appointmentRepository.existsByScheduledStartLessThanAndScheduledEndGreaterThan(
                    scheduledEnd,
                    scheduledStart
            );

            if (overlap) {
                return Result.failure(AppointmentCommandFailure.APPOINTMENT_ALREADY_EXISTS);
            }

            var appointment = new Appointment(
                    command.branchId(),
                    command.customerId(),
                    command.vehicleId(),
                    scheduledStart,
                    command.notes()
            );

            var savedAppointment = appointmentRepository.save(appointment);

            return Result.success(savedAppointment);

        } catch (IllegalArgumentException exception) {
            return Result.failure(AppointmentCommandFailure.INVALID_APPOINTMENT_DATA);
        }
    }
    @Override
    public Result<UUID, AppointmentCommandFailure> handle(DeleteAppointmentCommand command) {
        try {
            if (!appointmentRepository.existsById(command.appointmentId())) {
                return Result.failure(AppointmentCommandFailure.APPOINTMENT_NOT_FOUND);
            }

            appointmentRepository.deleteById(command.appointmentId());

            return Result.success(command.appointmentId());

        } catch (IllegalArgumentException exception) {
            return Result.failure(AppointmentCommandFailure.INVALID_APPOINTMENT_DATA);
        }
    }
}