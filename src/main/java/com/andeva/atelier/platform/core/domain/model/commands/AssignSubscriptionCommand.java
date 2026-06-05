package com.andeva.atelier.platform.core.domain.model.commands;

import com.andeva.atelier.platform.core.domain.model.valueobjects.BillingCycle;

import java.util.UUID;

public record AssignSubscriptionCommand(
        UUID branchId,
        UUID planId,
        BillingCycle billingCycle,
        String cardNumber,
        String cardHolderName,
        String expirationDate,
        String cvv
) {
    public AssignSubscriptionCommand {
        if (branchId == null) throw new IllegalArgumentException("core.error.branchId.required");
        if (planId == null) throw new IllegalArgumentException("core.error.planId.required");
        if (billingCycle == null) throw new IllegalArgumentException("core.error.billingCycle.required");
    }
}
