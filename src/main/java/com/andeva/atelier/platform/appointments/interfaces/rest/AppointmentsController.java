package com.andeva.atelier.platform.appointments.interfaces.rest;

import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandFailure;
import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandService;
import com.andeva.atelier.platform.appointments.interfaces.rest.resources.CreateAppointmentResource;
import com.andeva.atelier.platform.appointments.interfaces.rest.transform.AppointmentResourceFromAggregateAssembler;
import com.andeva.atelier.platform.appointments.interfaces.rest.transform.CreateAppointmentCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                appointment -> new ResponseEntity<>(
                        AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment),
                        HttpStatus.CREATED
                ),
                failure -> {
                    HttpStatus status = switch (failure) {
                        case APPOINTMENT_ALREADY_EXISTS -> HttpStatus.CONFLICT;
                        case INVALID_APPOINTMENT_DATA -> HttpStatus.BAD_REQUEST;
                    };

                    String detail = switch (failure) {
                        case APPOINTMENT_ALREADY_EXISTS -> "An appointment already exists in the selected schedule.";
                        case INVALID_APPOINTMENT_DATA -> "Invalid appointment data.";
                    };

                    return ResponseEntity
                            .status(status)
                            .body(ProblemDetail.forStatusAndDetail(status, detail));
                }
        );
    }
}