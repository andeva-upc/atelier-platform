package com.andeva.atelier.platform.operations.domain.model.commands;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import java.util.UUID;

/**
 * Command object representing the action of adding a Task to a Work Order in the operations domain.
 * This command encapsulates all necessary information to perform the action, including identifiers for the work order
 * @param workOrderId
 * @param serviceId
 * @param mechanicId
 * @param description
 * @param laborPrice
 * @author Joel Huamani Estefanero
 */
public record AddTaskToWorkOrderCommand(
        UUID workOrderId,
        ServiceId serviceId,
        MechanicId mechanicId,
        TaskDescription description,
        Money laborPrice
) {}
