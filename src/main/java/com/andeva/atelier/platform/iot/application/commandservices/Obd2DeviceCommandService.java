package com.andeva.atelier.platform.iot.application.commandservices;

import com.andeva.atelier.platform.iot.domain.model.aggregates.Obd2Device;
import com.andeva.atelier.platform.iot.domain.model.commands.CreateObd2DeviceCommand;
import com.andeva.atelier.platform.iot.domain.model.commands.DeleteObd2DeviceCommand;
import com.andeva.atelier.platform.shared.application.result.Result;

/**
 * Service interface for handling OBD2 Device creation and management commands.
 */
public interface Obd2DeviceCommandService {

    /**
     * Handles the creation/registration of a new OBD2 Device.
     * @param command the command containing device details
     * @return a Result containing the successfully registered OBD2 Device, or a Obd2DeviceCommandFailure
     */
    Result<Obd2Device, Obd2DeviceCommandFailure> handle(CreateObd2DeviceCommand command);

    /**
     * Handles the deletion/unregistration of an OBD2 Device.
     * @param command the command containing deletion details
     * @return a Result containing Void on success, or a Obd2DeviceCommandFailure
     */
    Result<Void, Obd2DeviceCommandFailure> handle(DeleteObd2DeviceCommand command);
}
