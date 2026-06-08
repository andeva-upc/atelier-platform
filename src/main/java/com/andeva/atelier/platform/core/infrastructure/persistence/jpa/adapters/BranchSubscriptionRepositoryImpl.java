package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.entities.BranchSubscription;
import com.andeva.atelier.platform.core.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.BranchSubscriptionId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.SubscriptionStatus;
import com.andeva.atelier.platform.core.domain.repositories.BranchSubscriptionRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.BranchSubscriptionPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.BranchSubscriptionPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.BranchSubscriptionPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BranchSubscriptionRepositoryImpl implements BranchSubscriptionRepository {

    private final BranchSubscriptionPersistenceRepository jpaRepository;

    public BranchSubscriptionRepositoryImpl(BranchSubscriptionPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public BranchSubscription save(BranchSubscription branchSubscription) {
        BranchSubscriptionPersistenceEntity entity = null;
        if (branchSubscription.getId() != null) {
            entity = jpaRepository.findById(branchSubscription.getId().value()).orElse(new BranchSubscriptionPersistenceEntity());
        } else {
            entity = new BranchSubscriptionPersistenceEntity();
        }
        BranchSubscriptionPersistenceAssembler.toEntity(branchSubscription, entity);
        BranchSubscriptionPersistenceEntity savedEntity = jpaRepository.save(entity);
        return BranchSubscriptionPersistenceAssembler.toDomain(savedEntity);
    }

    @Override
    public Optional<BranchSubscription> findById(BranchSubscriptionId id) {
        return jpaRepository.findById(id.value()).map(BranchSubscriptionPersistenceAssembler::toDomain);
    }

    @Override
    public List<BranchSubscription> findAllByBranchId(BranchId branchId) {
        return jpaRepository.findAllByBranchId(branchId.value()).stream()
                .map(BranchSubscriptionPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BranchSubscription> findActiveByBranchId(BranchId branchId) {
        return jpaRepository.findByBranchIdAndStatus(branchId.value(), SubscriptionStatus.ACTIVE)
                .map(BranchSubscriptionPersistenceAssembler::toDomain);
    }
}
