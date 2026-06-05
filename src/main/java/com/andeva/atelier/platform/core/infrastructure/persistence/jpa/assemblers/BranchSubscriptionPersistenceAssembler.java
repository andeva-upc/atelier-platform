package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.core.domain.model.entities.BranchSubscription;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.BranchSubscriptionPersistenceEntity;

public class BranchSubscriptionPersistenceAssembler {

    public static BranchSubscriptionPersistenceEntity toEntity(BranchSubscription branchSubscription, BranchSubscriptionPersistenceEntity entity) {
        if (entity == null) {
            entity = new BranchSubscriptionPersistenceEntity();
        }
        entity.setId(branchSubscription.getId());
        entity.setBranchId(branchSubscription.getBranchId());
        entity.setPlanId(branchSubscription.getPlanId());
        entity.setStatus(branchSubscription.getStatus());
        entity.setBillingCycle(branchSubscription.getBillingCycle());
        entity.setStartDate(branchSubscription.getStartDate());
        entity.setEndDate(branchSubscription.getEndDate());
        entity.setCanceledAt(branchSubscription.getCanceledAt());
        return entity;
    }

    public static BranchSubscription toDomain(BranchSubscriptionPersistenceEntity entity) {
        var branchSub = new BranchSubscription(
                entity.getBranchId(),
                entity.getPlanId(),
                entity.getBillingCycle(),
                entity.getStartDate(),
                entity.getEndDate()
        );
        branchSub.setStatus(entity.getStatus());
        branchSub.setCanceledAt(entity.getCanceledAt());

        try {
            var field = BranchSubscription.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(branchSub, entity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Could not set ID on domain object", e);
        }
        return branchSub;
    }
}
