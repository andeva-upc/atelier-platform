package com.andeva.atelier.platform.iot.interfaces.rest;

import com.andeva.atelier.platform.iot.application.commandservices.Obd2DeviceCommandService;
import com.andeva.atelier.platform.iot.interfaces.rest.resources.CreateObd2DeviceResource;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.CreateObd2DeviceCommandFromResourceAssembler;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.ResponseEntityFromObd2DeviceCommandResultAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing OBD2 Devices registration.
 */
@RestController
@RequestMapping(value = "/api/v1/obd2-devices", produces = "application/json")
@Tag(name = "OBD2 Devices", description = "Endpoints for managing OBD2 device fleet")
public class Obd2DevicesController {

    private final Obd2DeviceCommandService commandService;
    private final MessageSource messageSource;

    public Obd2DevicesController(Obd2DeviceCommandService commandService, MessageSource messageSource) {
        this.commandService = commandService;
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
}
