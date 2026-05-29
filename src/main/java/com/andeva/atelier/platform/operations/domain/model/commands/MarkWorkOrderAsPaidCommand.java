package com.andeva.atelier.platform.operations.domain.model.commands;

import java.util.UUID;
/**
 * Command representing the intent to mark a Work Order as paid.
 * Resides inside the domain commands package.
 *
 * @param workOrderId The unique identifier of the Work Order.
 */
public record MarkWorkOrderAsPaidCommand(UUID workOrderId) {}
