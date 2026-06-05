package com.andeva.atelier.platform.core.domain.repositories;

import com.andeva.atelier.platform.core.domain.model.aggregates.Branch;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BranchRepository {
    void save(Branch branch);
    Optional<Branch> findById(UUID id);
    List<Branch> findAllByWorkshopId(UUID workshopId);
    boolean existsById(UUID id);
    boolean existsByCode(String code);
}
