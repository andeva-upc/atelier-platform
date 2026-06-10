package com.andeva.atelier.platform.iot.interfaces.rest;

import com.andeva.atelier.platform.iot.application.commandservices.Obd2DeviceCommandFailure;
import com.andeva.atelier.platform.iot.application.commandservices.Obd2DeviceCommandService;
import com.andeva.atelier.platform.iot.application.queryservices.Obd2DeviceQueryService;
import com.andeva.atelier.platform.iot.domain.model.commands.CreateObd2DeviceCommand;
import com.andeva.atelier.platform.iot.domain.model.commands.DeleteObd2DeviceCommand;
import com.andeva.atelier.platform.iot.domain.model.queries.GetObd2DeviceByIdQuery;
import com.andeva.atelier.platform.iot.domain.model.valueobjects.Obd2DeviceId;
import com.andeva.atelier.platform.iot.interfaces.rest.resources.CreateObd2DeviceResource;
import com.andeva.atelier.platform.iot.interfaces.rest.resources.Obd2DeviceResource;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.CreateObd2DeviceCommandFromResourceAssembler;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.Obd2DeviceResourceFromAggregateAssembler;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.ResponseEntityFromObd2DeviceCommandResultAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

/**
 * REST controller for managing OBD2 Devices registration.
 */
@RestController
@RequestMapping(value = "/api/v1/obd2-devices", produces = "application/json")
@Tag(name = "OBD2 Devices", description = "Endpoints for managing OBD2 device fleet")
public class Obd2DevicesController {

    private final Obd2DeviceCommandService commandService;
    private final Obd2DeviceQueryService queryService;
    private final MessageSource messageSource;

    public Obd2DevicesController(
            Obd2DeviceCommandService commandService,
            Obd2DeviceQueryService queryService,
            MessageSource messageSource
    ) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.messageSource = messageSource;
    }

    /**
     * Registers a new OBD2 device.
     * @param resource the request payload containing branch ID and MAC address
     * @return a ResponseEntity containing the created resource or localized error details
     */
    @PostMapping
    @Operation(summary = "Register a new OBD2 device", description = "Registers a new physical OBD2 device in the specified branch")
    public ResponseEntity<?> createObd2Device(@Valid @RequestBody CreateObd2DeviceResource resource) {
        var command = CreateObd2DeviceCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityFromObd2DeviceCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    /**
     * Retrieves the details of a registered OBD2 device by its unique ID.
     * @param id the unique identifier of the OBD2 device
     * @return a ResponseEntity containing the device details or 404 if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get OBD2 device by ID", description = "Retrieves the details of a registered OBD2 device by its unique ID")
    public ResponseEntity<Obd2DeviceResource> getObd2DeviceById(@PathVariable UUID id) {
        var query = new GetObd2DeviceByIdQuery(new Obd2DeviceId(id));
        var obd2Device = queryService.handle(query);
        return obd2Device
                .map(device -> ResponseEntity.ok(Obd2DeviceResourceFromAggregateAssembler.toResourceFromAggregate(device)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Deletes (unregisters) an OBD2 device.
     * @param id the unique identifier of the OBD2 device
     * @return 204 No Content on success, or a ProblemDetail on failure
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an OBD2 device", description = "Performs a soft delete of an OBD2 device by its unique ID")
    public ResponseEntity<?> deleteObd2Device(@PathVariable UUID id) {
        var command = new DeleteObd2DeviceCommand(new Obd2DeviceId(id));
        var result = commandService.handle(command);

        return result.fold(
                _void -> ResponseEntity.noContent().build(),
                failure -> {
                    HttpStatus status = switch (failure) {
                        case Obd2DeviceCommandFailure.NotFound(String _) -> HttpStatus.NOT_FOUND;
                        case Obd2DeviceCommandFailure.InvalidState(String _) -> HttpStatus.BAD_REQUEST;
                        case Obd2DeviceCommandFailure.Duplicate(String _) -> HttpStatus.CONFLICT;
                    };
                    String messageKey = switch (failure) {
                        case Obd2DeviceCommandFailure.NotFound(String message) -> message;
                        case Obd2DeviceCommandFailure.InvalidState(String message) -> message;
                        case Obd2DeviceCommandFailure.Duplicate(String message) -> message;
                    };
                    String localizedMessage;
                    try {
                        localizedMessage = messageSource.getMessage(messageKey, null, Locale.getDefault());
                    } catch (Exception e) {
                        localizedMessage = messageKey;
                    }
                    return ResponseEntity.status(status).body(
                            ProblemDetail.forStatusAndDetail(status, localizedMessage)
                    );
                }
        );
    }
}
