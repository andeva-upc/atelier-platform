package com.andeva.atelier.platform.core.domain.repositories;

import com.andeva.atelier.platform.core.domain.model.aggregates.Owner;

import java.util.Optional;
import java.util.UUID;

public interface OwnerRepository {
    void save(Owner owner);
    Optional<Owner> findById(UUID id);
    Optional<Owner> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
    void delete(Owner owner);
}
