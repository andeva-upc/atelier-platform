package com.andeva.atelier.platform.operations.domain.model.commands;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import java.util.UUID;

/**
 * Command representing the intent to update the quantity of a Product in a Task within a Work Order.
 * @param workOrderId
 * @param taskId
 * @param productId
 * @param newQuantity
 * @author Joel Huamani Estefanero
 */
public record UpdateProductQuantityInTaskCommand(
        UUID workOrderId,
        UUID taskId,
        ProductId productId,
        Quantity newQuantity
) {}
