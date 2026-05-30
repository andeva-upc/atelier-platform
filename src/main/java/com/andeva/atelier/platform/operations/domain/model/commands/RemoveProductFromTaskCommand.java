package com.andeva.atelier.platform.operations.domain.model.commands;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.ProductId;
import java.util.UUID;

/**
 * Command to remove a Product from a Task within a Work Order. This command encapsulates the necessary identifiers to perform the operation, including the Work Order ID, Task ID, and Product ID.
 * @param workOrderId
 * @param taskId
 * @param productId
 * @author Joel Huamani Estefanero
 */
public record RemoveProductFromTaskCommand(
        UUID workOrderId,
        UUID taskId,
        ProductId productId
) {}