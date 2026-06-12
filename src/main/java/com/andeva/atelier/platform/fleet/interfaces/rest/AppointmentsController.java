package com.andeva.atelier.platform.fleet.interfaces.rest;

import com.andeva.atelier.platform.fleet.application.commandservices.AppointmentCommandFailure;
import com.andeva.atelier.platform.fleet.application.commandservices.AppointmentCommandService;
import com.andeva.atelier.platform.fleet.application.queryservices.AppointmentQueryFailure;
import com.andeva.atelier.platform.fleet.application.queryservices.AppointmentQueryService;
import com.andeva.atelier.platform.fleet.domain.model.commands.DeleteAppointmentCommand;
import com.andeva.atelier.platform.fleet.interfaces.rest.resources.AppointmentResource;
import com.andeva.atelier.platform.fleet.interfaces.rest.resources.CreateAppointmentResource;
import com.andeva.atelier.platform.fleet.interfaces.rest.resources.UpdateAppointmentResource;
import com.andeva.atelier.platform.fleet.interfaces.rest.transform.AppointmentResourceFromAggregateAssembler;
import com.andeva.atelier.platform.fleet.interfaces.rest.transform.CreateAppointmentCommandFromResourceAssembler;
import com.andeva.atelier.platform.fleet.interfaces.rest.transform.UpdateAppointmentCommandFromResourceAssembler;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final AppointmentQueryService queryService;

    public AppointmentsController(AppointmentCommandService commandService,
                                   AppointmentQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    @Operation(summary = "Create a new appointment", description = "Creates a new appointment")
    public ResponseEntity<?> createAppointment(@Valid @RequestBody CreateAppointmentResource resource) {
        var command = CreateAppointmentCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return result.fold(
                appointment -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment)),
                this::handleCommandFailure
        );
    }

    @PutMapping("/{appointmentId}")
    @Operation(summary = "Update an appointment", description = "Updates an existing appointment by ID")
    public ResponseEntity<?> updateAppointment(
            @PathVariable UUID appointmentId,
            @Valid @RequestBody UpdateAppointmentResource resource) {
        var command = UpdateAppointmentCommandFromResourceAssembler
                .toCommandFromResource(appointmentId, resource);
        var result = commandService.handle(command);
        return result.fold(
                appointment -> ResponseEntity.ok(
                        AppointmentResourceFromAggregateAssembler.toResourceFromAggregate(appointment)),
                this::handleCommandFailure
        );
    }

    @DeleteMapping("/{appointmentId}")
    @Operation(summary = "Delete an appointment", description = "Soft deletes an appointment by ID")
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID appointmentId) {
        var command = new DeleteAppointmentCommand(appointmentId);
        var result = commandService.handle(command);
        return result.fold(
                deletedId -> ResponseEntity.noContent().build(),
                this::handleCommandFailure
        );
    }

    @GetMapping("/branch/{branchId}")
    @Operation(
            summary = "Get appointments by branch",
            description = "Returns all active appointments for a given branch ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointments retrieved successfully",
                    content = @Content(array = @ArraySchema(
                            schema = @Schema(implementation = AppointmentResource.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid branch ID",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<?> getByBranch(@PathVariable UUID branchId) {
        var result = queryService.handle(new BranchId(branchId));
        return result.fold(
                appointments -> ResponseEntity.ok(
                        appointments.stream()
                                .map(AppointmentResourceFromAggregateAssembler::toResourceFromAggregate)
                                .toList()),
                this::handleQueryFailure
        );
    }

    private ResponseEntity<ProblemDetail> handleCommandFailure(AppointmentCommandFailure failure) {
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
        return ResponseEntity.status(status)
                .body(ProblemDetail.forStatusAndDetail(status, detail));
    }

    private ResponseEntity<ProblemDetail> handleQueryFailure(AppointmentQueryFailure failure) {
        HttpStatus status = switch (failure) {
            case APPOINTMENT_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_QUERY_PARAMS -> HttpStatus.BAD_REQUEST;
        };
        String detail = switch (failure) {
            case APPOINTMENT_NOT_FOUND -> "Appointment not found.";
            case INVALID_QUERY_PARAMS -> "Invalid query parameters.";
        };
        return ResponseEntity.status(status)
                .body(ProblemDetail.forStatusAndDetail(status, detail));
    }
}