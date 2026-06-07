package com.andeva.atelier.platform.core.domain.model.commands;

import com.andeva.atelier.platform.core.domain.model.valueobjects.BranchId;

public record CancelSubscriptionCommand(
        BranchId branchId
) {
}
