package com.andeva.atelier.platform.iot.interfaces.rest;

import com.andeva.atelier.platform.iot.application.commandservices.Obd2DeviceRegistrationCommandService;
import com.andeva.atelier.platform.iot.interfaces.rest.resources.LinkObd2DeviceResource;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.LinkObd2DeviceCommandFromResourceAssembler;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.ResponseEntityFromObd2DeviceRegistrationCommandResultAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing OBD2 Device Registrations (linking devices to vehicles).
 */
@RestController
@RequestMapping(value = "/api/v1/obd2-device-registrations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "OBD2 Device Registrations", description = "Endpoints for managing OBD2-vehicle couplings")
public class Obd2DeviceRegistrationsController {

    private final Obd2DeviceRegistrationCommandService commandService;
    private final MessageSource messageSource;

    public Obd2DeviceRegistrationsController(
            Obd2DeviceRegistrationCommandService commandService,
            MessageSource messageSource
    ) {
        this.commandService = commandService;
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
}
