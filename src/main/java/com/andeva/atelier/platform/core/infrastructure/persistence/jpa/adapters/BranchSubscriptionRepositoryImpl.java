package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.entities.BranchSubscription;
import com.andeva.atelier.platform.core.domain.model.valueobjects.SubscriptionStatus;
import com.andeva.atelier.platform.core.domain.repositories.BranchSubscriptionRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.BranchSubscriptionPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.BranchSubscriptionJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BranchSubscriptionRepositoryImpl implements BranchSubscriptionRepository {

    private final BranchSubscriptionJpaRepository jpaRepository;

    public BranchSubscriptionRepositoryImpl(BranchSubscriptionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(BranchSubscription branchSubscription) {
        BranchSubscriptionPersistenceEntity entity = jpaRepository.findById(branchSubscription.getId()).orElse(new BranchSubscriptionPersistenceEntity());
        entity.setId(branchSubscription.getId());
        entity.setBranchId(branchSubscription.getBranchId());
        entity.setPlanId(branchSubscription.getPlanId());
        entity.setStatus(branchSubscription.getStatus());
        entity.setBillingCycle(branchSubscription.getBillingCycle());
        entity.setStartDate(branchSubscription.getStartDate());
        entity.setEndDate(branchSubscription.getEndDate());
        entity.setCanceledAt(branchSubscription.getCanceledAt());

        jpaRepository.save(entity);
    }

    @Override
    public Optional<BranchSubscription> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<BranchSubscription> findAllByBranchId(UUID branchId) {
        return jpaRepository.findAllByBranchId(branchId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BranchSubscription> findActiveByBranchId(UUID branchId) {
        return jpaRepository.findByBranchIdAndStatus(branchId, SubscriptionStatus.ACTIVE)
                .map(this::toDomain);
    }

    private BranchSubscription toDomain(BranchSubscriptionPersistenceEntity entity) {
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
            // Because BranchSubscription doesn't inherit from AbstractDomainAggregateRoot (it's just an entity inside Branch conceptually or handled independently here)
            // Wait, does it? Let me check its ID field.
            var field = BranchSubscription.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(branchSub, entity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Could not set ID on domain object", e);
        }
        return branchSub;
    }
}
