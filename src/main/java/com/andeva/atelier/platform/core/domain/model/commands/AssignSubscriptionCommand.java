package com.andeva.atelier.platform.core.domain.model.commands;

import com.andeva.atelier.platform.core.domain.model.valueobjects.BillingCycle;
import com.andeva.atelier.platform.core.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.SubscriptionPlanId;

public record AssignSubscriptionCommand(
        BranchId branchId,
        SubscriptionPlanId planId,
        BillingCycle billingCycle,
        String cardNumber,
        String cardHolderName,
        String expirationDate,
        String cvv
) {
    public AssignSubscriptionCommand {
        if (billingCycle == null) throw new IllegalArgumentException("core.error.billingCycle.required");
    }
}
