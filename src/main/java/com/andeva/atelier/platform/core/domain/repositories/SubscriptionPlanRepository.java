package com.andeva.atelier.platform.core.domain.repositories;

import com.andeva.atelier.platform.core.domain.model.aggregates.SubscriptionPlan;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionPlanRepository {
    Optional<SubscriptionPlan> findById(UUID id);
    Optional<SubscriptionPlan> findByName(String name);
    List<SubscriptionPlan> findAll();
}
