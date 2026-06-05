package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.SubscriptionPlan;
import com.andeva.atelier.platform.core.domain.repositories.SubscriptionPlanRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.SubscriptionPlanPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.SubscriptionPlanJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SubscriptionPlanRepositoryImpl implements SubscriptionPlanRepository {

    private final SubscriptionPlanJpaRepository jpaRepository;

    public SubscriptionPlanRepositoryImpl(SubscriptionPlanJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<SubscriptionPlan> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<SubscriptionPlan> findByName(String name) {
        return jpaRepository.findByName(name).map(this::toDomain);
    }

    @Override
    public List<SubscriptionPlan> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private SubscriptionPlan toDomain(SubscriptionPlanPersistenceEntity entity) {
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
