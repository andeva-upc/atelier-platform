package com.andeva.atelier.platform.core.domain.repositories;

import com.andeva.atelier.platform.core.domain.model.entities.BranchSubscription;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BranchSubscriptionRepository {
    void save(BranchSubscription branchSubscription);
    Optional<BranchSubscription> findById(UUID id);
    List<BranchSubscription> findAllByBranchId(UUID branchId);
    Optional<BranchSubscription> findActiveByBranchId(UUID branchId);
}
