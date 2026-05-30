package com.andeva.atelier.platform.operations.domain.model.commands;

import java.util.UUID;

/**
 * Command to reopen a task within a work order. This command is used when a task that was previously closed needs to be reopened for further work or corrections.
 * It contains the identifiers for both the work order and the specific task that is to be reopened
 * @param workOrderId
 * @param taskId
 * @author Joel Huamani Estefanero
 */
public record ReopenTaskCommand(UUID workOrderId, UUID taskId) {}
