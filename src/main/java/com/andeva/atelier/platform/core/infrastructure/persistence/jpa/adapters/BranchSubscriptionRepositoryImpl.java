package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.entities.BranchSubscription;
import com.andeva.atelier.platform.core.domain.model.valueobjects.SubscriptionStatus;
import com.andeva.atelier.platform.core.domain.repositories.BranchSubscriptionRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.BranchSubscriptionPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.BranchSubscriptionPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.BranchSubscriptionPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BranchSubscriptionRepositoryImpl implements BranchSubscriptionRepository {

    private final BranchSubscriptionPersistenceRepository jpaRepository;

    public BranchSubscriptionRepositoryImpl(BranchSubscriptionPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(BranchSubscription branchSubscription) {
        BranchSubscriptionPersistenceEntity entity = jpaRepository.findById(branchSubscription.getId()).orElse(new BranchSubscriptionPersistenceEntity());
        BranchSubscriptionPersistenceAssembler.toEntity(branchSubscription, entity);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<BranchSubscription> findById(UUID id) {
        return jpaRepository.findById(id).map(BranchSubscriptionPersistenceAssembler::toDomain);
    }

    @Override
    public List<BranchSubscription> findAllByBranchId(UUID branchId) {
        return jpaRepository.findAllByBranchId(branchId).stream()
                .map(BranchSubscriptionPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BranchSubscription> findActiveByBranchId(UUID branchId) {
        return jpaRepository.findByBranchIdAndStatus(branchId, SubscriptionStatus.ACTIVE)
                .map(BranchSubscriptionPersistenceAssembler::toDomain);
    }
}
