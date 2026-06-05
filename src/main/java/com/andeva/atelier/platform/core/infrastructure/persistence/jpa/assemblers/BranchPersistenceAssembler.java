package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.core.domain.model.aggregates.Branch;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.BranchPersistenceEntity;

public class BranchPersistenceAssembler {

    public static BranchPersistenceEntity toEntity(Branch branch, BranchPersistenceEntity entity) {
        if (entity == null) {
            entity = new BranchPersistenceEntity();
        }
        entity.setId(branch.getId());
        entity.setWorkshopId(branch.getWorkshopId());
        entity.setCode(branch.getCode());
        entity.setName(branch.getName());
        if (branch.getAddress() != null) {
            entity.setAddress(branch.getAddress().value());
        }
        entity.setPhone(branch.getPhone());
        return entity;
    }

    public static Branch toDomain(BranchPersistenceEntity entity) {
        var branch = new Branch(
                entity.getWorkshopId(),
                entity.getCode(),
                entity.getName(),
                entity.getAddress(),
                entity.getPhone()
        );

        try {
            var field = com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(branch, entity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Could not set ID on domain object", e);
        }
        return branch;
    }
}
