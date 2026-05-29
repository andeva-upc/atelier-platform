package com.andeva.atelier.platform.operations.domain.model.commands;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import java.util.UUID;

public record UpdateProductQuantityInTaskCommand(
        UUID workOrderId,
        UUID taskId,
        ProductId productId,
        Quantity newQuantity
) {}
