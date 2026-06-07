package com.andeva.atelier.platform.billing.domain.model.commands;

import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import java.util.UUID;

public record CreateQuoteCommand(
        UUID workOrderId,
        BranchId branchId,
        Double discountPercentage
) {
    public CreateQuoteCommand {
        if (workOrderId == null) {
            throw new IllegalArgumentException("Work Order ID cannot be null");
        }
        if (branchId == null) {
            throw new IllegalArgumentException("Branch ID cannot be null");
        }
        if (discountPercentage == null || discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
    }
}
