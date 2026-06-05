package com.andeva.atelier.platform.core.domain.repositories;

import com.andeva.atelier.platform.core.domain.model.aggregates.Workshop;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkshopRepository {
    void save(Workshop workshop);
    Optional<Workshop> findById(UUID id);
    List<Workshop> findAllByOwnerId(UUID ownerId);
    boolean existsById(UUID id);
}
