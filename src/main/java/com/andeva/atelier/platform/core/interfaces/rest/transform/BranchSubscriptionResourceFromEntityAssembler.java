package com.andeva.atelier.platform.core.interfaces.rest.transform;

import com.andeva.atelier.platform.core.domain.model.entities.BranchSubscription;
import com.andeva.atelier.platform.core.interfaces.rest.resources.BranchSubscriptionResource;

public class BranchSubscriptionResourceFromEntityAssembler {
    public static BranchSubscriptionResource toResourceFromEntity(BranchSubscription entity) {
        return new BranchSubscriptionResource(
                entity.getId(),
                entity.getBranchId(),
                entity.getPlanId(),
                entity.getBillingCycle().name(),
                entity.getStatus().name(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }
}
