package com.andeva.atelier.platform.operations.domain.model.commands;

import java.util.UUID;

public record StartTaskCommand(UUID workOrderId, UUID taskId) {}
