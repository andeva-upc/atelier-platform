package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.core.domain.model.aggregates.Workshop;
import com.andeva.atelier.platform.core.domain.model.valueobjects.OwnerId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.WorkshopId;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.WorkshopPersistenceEntity;

public class WorkshopPersistenceAssembler {

    public static WorkshopPersistenceEntity toEntity(Workshop workshop, WorkshopPersistenceEntity entity) {
        if (entity == null) {
            entity = new WorkshopPersistenceEntity();
        }
        entity.setId(workshop.getId() != null ? workshop.getId().value() : null);
        entity.setOwnerId(workshop.getOwnerId() != null ? workshop.getOwnerId().value() : null);
        entity.setBusinessName(workshop.getBusinessName());
        entity.setBrandName(workshop.getBrandName());
        entity.setTaxId(workshop.getTaxId());
        entity.setMileageIntervalConfig(workshop.getMileageIntervalConfig());
        return entity;
    }

    public static Workshop toDomain(WorkshopPersistenceEntity entity) {
        return new Workshop(
                new WorkshopId(entity.getId()),
                new OwnerId(entity.getOwnerId()),
                entity.getBusinessName(),
                entity.getBrandName(),
                entity.getTaxId(),
                entity.getMileageIntervalConfig()
        );
    }
}
