package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.core.domain.model.aggregates.SubscriptionPlan;
import com.andeva.atelier.platform.core.domain.model.valueobjects.SubscriptionPlanId;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.SubscriptionPlanPersistenceEntity;

public class SubscriptionPlanPersistenceAssembler {

    public static SubscriptionPlan toDomain(SubscriptionPlanPersistenceEntity entity) {
        return new SubscriptionPlan(
                new SubscriptionPlanId(entity.getId()),
                entity.getName(),
                entity.getMonthlyPrice(),
                entity.getMaxObd2Devices(),
                entity.getMaxMonthlySnapshotsPerVehicle(),
                entity.getMaxCustomers(),
                entity.getMaxStaffAccounts(),
                entity.isActive()
        );
    }
}
