package com.andeva.atelier.platform.operations.domain.model.commands;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import java.util.UUID;

/**
 * Command representing the intent to update the details of a Task within a Work Order.
 * This command encapsulates all necessary information to perform the update action, including identifiers for the work order and task, as well as the new details to be applied.
 * @param workOrderId
 * @param taskId
 * @param serviceId
 * @param mechanicId
 * @param description
 * @param laborPrice
 * @author Joel Huamani Estefanero
 */
public record UpdateWorkOrderTaskDetailsCommand(
        UUID workOrderId,
        UUID taskId,
        ServiceId serviceId,
        MechanicId mechanicId,
        TaskDescription description,
        Money laborPrice
) {}
