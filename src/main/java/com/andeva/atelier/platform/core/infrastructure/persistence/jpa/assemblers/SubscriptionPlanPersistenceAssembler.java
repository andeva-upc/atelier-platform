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

        try {
            var field = com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(plan, entity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Could not set ID on domain object", e);
        }
        return plan;
    }
}
