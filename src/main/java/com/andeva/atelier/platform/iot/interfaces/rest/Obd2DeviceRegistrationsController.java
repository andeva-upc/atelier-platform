package com.andeva.atelier.platform.iot.interfaces.rest;

import com.andeva.atelier.platform.iot.application.commandservices.Obd2DeviceRegistrationCommandService;
import com.andeva.atelier.platform.iot.application.queryservices.Obd2DeviceRegistrationQueryService;
import com.andeva.atelier.platform.iot.application.queryservices.TelemetryQueryService;
import com.andeva.atelier.platform.iot.domain.model.commands.DeactivateObd2DeviceRegistrationCommand;
import com.andeva.atelier.platform.iot.domain.model.queries.GetObd2DeviceRegistrationsByBranchIdAndStatusQuery;
import com.andeva.atelier.platform.iot.domain.model.queries.GetTelemetrySnapshotsByRegistrationIdQuery;
import com.andeva.atelier.platform.iot.domain.model.valueobjects.Obd2DeviceRegistrationId;
import com.andeva.atelier.platform.iot.domain.model.valueobjects.Obd2RegistrationStatus;
import com.andeva.atelier.platform.iot.interfaces.rest.resources.LinkObd2DeviceResource;
import com.andeva.atelier.platform.iot.interfaces.rest.resources.Obd2DeviceRegistrationResource;
import com.andeva.atelier.platform.iot.interfaces.rest.resources.TelemetrySnapshotResource;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.LinkObd2DeviceCommandFromResourceAssembler;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.Obd2DeviceRegistrationResourceFromAggregateAssembler;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.ResponseEntityFromObd2DeviceRegistrationCommandResultAssembler;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.TelemetrySnapshotResourceFromAggregateAssembler;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing OBD2 Device Registrations (linking devices to vehicles).
 */
@RestController
@RequestMapping(value = "/api/v1/obd2-device-registrations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "OBD2 Device Registrations", description = "Endpoints for managing OBD2-vehicle couplings")
public class Obd2DeviceRegistrationsController {

    private final Obd2DeviceRegistrationCommandService commandService;
    private final Obd2DeviceRegistrationQueryService queryService;
    private final TelemetryQueryService telemetryQueryService;
    private final MessageSource messageSource;

    public Obd2DeviceRegistrationsController(
            Obd2DeviceRegistrationCommandService commandService,
            Obd2DeviceRegistrationQueryService queryService,
            TelemetryQueryService telemetryQueryService,
            MessageSource messageSource
    ) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.telemetryQueryService = telemetryQueryService;
        this.messageSource = messageSource;
    }

    /**
     * Links an OBD2 device to a vehicle.
     * @param resource the request payload containing device, branch and vehicle IDs
     * @return a ResponseEntity containing the created registration resource or localized error details
     */
    @PostMapping
    @Operation(summary = "Link OBD2 device to vehicle", description = "Links a registered OBD2 device to a specific vehicle inside a branch")
    public ResponseEntity<?> linkObd2Device(@Valid @RequestBody LinkObd2DeviceResource resource) {
        var command = LinkObd2DeviceCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityFromObd2DeviceRegistrationCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    /**
     * Deactivates/unlinks an OBD2 device registration.
     * @param id the registration UUID
     * @return a ResponseEntity containing the deactivated registration resource or localized error details
     */
    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate OBD2 device registration", description = "Deactivates/unlinks an active OBD2-vehicle coupling")
    public ResponseEntity<?> deactivateObd2DeviceRegistration(@PathVariable UUID id) {
        var command = new DeactivateObd2DeviceRegistrationCommand(new Obd2DeviceRegistrationId(id));
        var result = commandService.handle(command);
        return ResponseEntityFromObd2DeviceRegistrationCommandResultAssembler.toResponseEntityFromResult(result, HttpStatus.OK, messageSource);
    }

    /**
     * Retrieves OBD2 device registrations by branch and status.
     * @param branchId the branch identifier
     * @param status the registration status string
     * @return a ResponseEntity containing the list of matching registration resources
     */
    @GetMapping
    @Operation(summary = "Get OBD2 device registrations by branch and status", description = "Retrieves all registered OBD2-vehicle couplings under a specific branch, filtered by status")
    public ResponseEntity<List<Obd2DeviceRegistrationResource>> getObd2DeviceRegistrations(
            @RequestParam UUID branchId,
            @RequestParam String status
    ) {
        var query = new GetObd2DeviceRegistrationsByBranchIdAndStatusQuery(
                new BranchId(branchId),
                new Obd2RegistrationStatus(status)
        );
        var list = queryService.handle(query);
        var resources = list.stream()
                .map(Obd2DeviceRegistrationResourceFromAggregateAssembler::toResourceFromAggregate)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Retrieves telemetry snapshots for a specific OBD2 device registration.
     * @param id the registration identifier
     * @return a ResponseEntity containing the list of telemetry snapshot resources
     */
    @GetMapping("/{id}/telemetry-snapshots")
    @Operation(summary = "Get telemetry snapshots for registration", description = "Retrieves all telemetry snapshots captured under a specific OBD2-vehicle registration")
    public ResponseEntity<List<TelemetrySnapshotResource>> getTelemetrySnapshotsForRegistration(@PathVariable UUID id) {
        var query = new GetTelemetrySnapshotsByRegistrationIdQuery(new Obd2DeviceRegistrationId(id));
        var list = telemetryQueryService.handle(query);
        var resources = list.stream()
                .map(TelemetrySnapshotResourceFromAggregateAssembler::toResourceFromAggregate)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
