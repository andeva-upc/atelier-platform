package com.andeva.atelier.platform.operations.domain.model.events;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.ProductId;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.Quantity;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;

public record ProductReservedEvent(
        Object source,
        BranchId branchId,
        ProductId productId,
        Quantity quantity
) {}
