package com.andeva.atelier.platform.operations.domain.model.commands;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import java.util.UUID;

public record AddProductToTaskCommand(
        UUID workOrderId,
        UUID taskId,
        ProductId productId,
        Quantity quantity,
        Money unitPrice
) {}
