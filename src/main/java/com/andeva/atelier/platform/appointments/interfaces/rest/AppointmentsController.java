package com.andeva.atelier.platform.appointments.interfaces.rest;

import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandFailure;
import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandService;
import com.andeva.atelier.platform.appointments.domain.model.commands.DeleteAppointmentCommand;
import com.andeva.atelier.platform.appointments.interfaces.rest.resources.CreateAppointmentResource;
import com.andeva.atelier.platform.appointments.interfaces.rest.resources.UpdateAppointmentResource;
import com.andeva.atelier.platform.appointments.interfaces.rest.transform.AppointmentResourceFromAggregateAssembler;
import com.andeva.atelier.platform.appointments.interfaces.rest.transform.CreateAppointmentCommandFromResourceAssembler;
import com.andeva.atelier.platform.appointments.interfaces.rest.transform.UpdateAppointmentCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/appointments", produces = "application/json")
@Tag(name = "Appointments", description = "Appointment Management Endpoints")
public class AppointmentsController {

    private final AppointmentCommandService commandService;

    public AppointmentsController(AppointmentCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new appointment",
            description = "Creates a new appointment"
    )
    public ResponseEntity<?> createAppointment(@Valid @RequestBody CreateAppointmentResource resource) {
        var command = CreateAppointmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);

        return result.fold(
                appointment -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment)),
                this::handleFailure
        );
    }

    @DeleteMapping("/{appointmentId}")
    @Operation(
            summary = "Delete an appointment",
            description = "Soft deletes an appointment by ID"
    )
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID appointmentId) {
        var command = new DeleteAppointmentCommand(appointmentId);
        var result = commandService.handle(command);

        return result.fold(
                deletedAppointmentId -> ResponseEntity.noContent().build(),
                this::handleFailure
        );
    }

    private ResponseEntity<ProblemDetail> handleFailure(AppointmentCommandFailure failure) {
        HttpStatus status = switch (failure) {
            case APPOINTMENT_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case APPOINTMENT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_APPOINTMENT_DATA -> HttpStatus.BAD_REQUEST;
        };

        String detail = switch (failure) {
            case APPOINTMENT_ALREADY_EXISTS -> "An appointment already exists in the selected schedule.";
            case APPOINTMENT_NOT_FOUND -> "Appointment not found.";
            case INVALID_APPOINTMENT_DATA -> "Invalid appointment data.";
        };

        return ResponseEntity
                .status(status)
                .body(ProblemDetail.forStatusAndDetail(status, detail));
    }
    @PutMapping("/{appointmentId}")
    @Operation(summary = "Update an appointment", description = "Updates an existing appointment by ID")
    public ResponseEntity<?> updateAppointment(
            @PathVariable UUID appointmentId,
            @Valid @RequestBody UpdateAppointmentResource resource
    ) {
        var command = UpdateAppointmentCommandFromResourceAssembler.toCommandFromResource(appointmentId, resource);
        var result = commandService.handle(command);

        return result.fold(
                appointment -> ResponseEntity.ok(
                        AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment)
                ),
                this::handleFailure
        );
    }
}