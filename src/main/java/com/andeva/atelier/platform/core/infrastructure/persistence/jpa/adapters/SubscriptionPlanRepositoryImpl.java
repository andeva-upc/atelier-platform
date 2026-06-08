package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.SubscriptionPlan;
import com.andeva.atelier.platform.core.domain.model.valueobjects.SubscriptionPlanId;
import com.andeva.atelier.platform.core.domain.repositories.SubscriptionPlanRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.SubscriptionPlanPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.SubscriptionPlanPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.SubscriptionPlanPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SubscriptionPlanRepositoryImpl implements SubscriptionPlanRepository {

    private final SubscriptionPlanPersistenceRepository jpaRepository;

    public SubscriptionPlanRepositoryImpl(SubscriptionPlanPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public SubscriptionPlan save(SubscriptionPlan subscriptionPlan) {
        SubscriptionPlanPersistenceEntity entity = null;
        if (subscriptionPlan.getId() != null) {
            entity = jpaRepository.findById(subscriptionPlan.getId().value()).orElse(new SubscriptionPlanPersistenceEntity());
        } else {
            entity = new SubscriptionPlanPersistenceEntity();
        }
        SubscriptionPlanPersistenceAssembler.toEntity(subscriptionPlan, entity);
        SubscriptionPlanPersistenceEntity savedEntity = jpaRepository.save(entity);
        return SubscriptionPlanPersistenceAssembler.toDomain(savedEntity);
    }

    @Override
    public Optional<SubscriptionPlan> findById(SubscriptionPlanId id) {
        return jpaRepository.findById(id.value()).map(SubscriptionPlanPersistenceAssembler::toDomain);
    }

    @Override
    public Optional<SubscriptionPlan> findByName(String name) {
        return jpaRepository.findByName(name).map(SubscriptionPlanPersistenceAssembler::toDomain);
    }

    @Override
    public List<SubscriptionPlan> findAll() {
        return jpaRepository.findAll().stream()
                .map(SubscriptionPlanPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }
}
