package com.andeva.atelier.platform.appointments.interfaces.rest;

import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandFailure;
import com.andeva.atelier.platform.appointments.application.commandservices.AppointmentCommandService;
import com.andeva.atelier.platform.appointments.interfaces.rest.resources.CreateAppointmentResource;
import com.andeva.atelier.platform.appointments.interfaces.rest.transform.CreateAppointmentCommandFromResourceAssembler;
import com.andeva.atelier.platform.appointments.interfaces.rest.transform.AppointmentResourceFromAggregateAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> createAppointment(
            @Valid @RequestBody CreateAppointmentResource resource) {

        var command =
                CreateAppointmentCommandFromResourceAssembler
                        .toCommandFromResource(resource);

        var result = commandService.handle(command);

        if (result.isSuccess()) {
            var appointmentResource =
                    AppointmentResourceFromAggregateAssembler
                            .toResourceFromAggregate(result.success().get());

            return new ResponseEntity<>(
                    appointmentResource,
                    HttpStatus.CREATED
            );
        }

        return ResponseEntity
                .badRequest()
                .body("Invalid appointment data");
    }
}