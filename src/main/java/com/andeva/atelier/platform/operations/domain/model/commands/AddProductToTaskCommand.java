package com.andeva.atelier.platform.operations.domain.model.commands;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import java.util.UUID;

/**
 * Command representing the intent to add a Product to a Task within a Work Order.
 * Resides inside the domain commands package.
 * @param workOrderId
 * @param taskId
 * @param productId
 * @param quantity
 * @param unitPrice
 * @author Joel Huamani Estefanero
 */
public record AddProductToTaskCommand(
        UUID workOrderId,
        UUID taskId,
        ProductId productId,
        Quantity quantity,
        Money unitPrice
) {}
