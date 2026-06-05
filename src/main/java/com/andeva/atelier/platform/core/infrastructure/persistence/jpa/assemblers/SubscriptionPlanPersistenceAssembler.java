package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.core.domain.model.aggregates.SubscriptionPlan;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.SubscriptionPlanPersistenceEntity;

public class SubscriptionPlanPersistenceAssembler {

    public static SubscriptionPlan toDomain(SubscriptionPlanPersistenceEntity entity) {
        var plan = new SubscriptionPlan();
        plan.setName(entity.getName());
        plan.setMonthlyPrice(entity.getMonthlyPrice());
        plan.setMaxObd2Devices(entity.getMaxObd2Devices());
        plan.setMaxMonthlySnapshotsPerVehicle(entity.getMaxMonthlySnapshotsPerVehicle());
        plan.setMaxCustomers(entity.getMaxCustomers());
        plan.setMaxStaffAccounts(entity.getMaxStaffAccounts());
        plan.setActive(entity.isActive());

                plan.setId(entity.getId());
        return plan;
    }
}
