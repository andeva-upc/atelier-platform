package com.andeva.atelier.platform.operations.domain.model.commands;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.ProductId;
import java.util.UUID;

public record RemoveProductFromTaskCommand(
        UUID workOrderId,
        UUID taskId,
        ProductId productId
) {}