package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.core.domain.model.aggregates.Branch;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Phone;
import com.andeva.atelier.platform.core.domain.model.valueobjects.WorkshopId;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.BranchPersistenceEntity;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Address;

public final class BranchPersistenceAssembler {

    private BranchPersistenceAssembler() {}

    public static BranchPersistenceEntity toEntity(Branch branch, BranchPersistenceEntity entity) {
        if (entity == null) {
            entity = new BranchPersistenceEntity();
        }
        entity.setId(branch.getId() != null ? branch.getId().value() : null);
        entity.setWorkshopId(branch.getWorkshopId() != null ? branch.getWorkshopId().value() : null);
        entity.setCode(branch.getCode());
        entity.setName(branch.getName());
        if (branch.getAddress() != null) {
            entity.setAddress(branch.getAddress().value());
        }
        entity.setPhone(branch.getPhone() != null ? branch.getPhone().value() : null);
        return entity;
    }

    public static Branch toDomain(BranchPersistenceEntity entity) {
        return new Branch(
                new BranchId(entity.getId()),
                new WorkshopId(entity.getWorkshopId()),
                entity.getCode(),
                entity.getName(),
                new Address(entity.getAddress()),
                new Phone(entity.getPhone())
        );
    }
}

