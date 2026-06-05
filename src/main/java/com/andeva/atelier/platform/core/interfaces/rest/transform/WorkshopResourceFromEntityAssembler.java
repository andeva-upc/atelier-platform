package com.andeva.atelier.platform.core.interfaces.rest.transform;

import com.andeva.atelier.platform.core.domain.model.aggregates.Workshop;
import com.andeva.atelier.platform.core.interfaces.rest.resources.WorkshopResource;

public class WorkshopResourceFromEntityAssembler {
    public static WorkshopResource toResourceFromEntity(Workshop entity) {
        return new WorkshopResource(
                entity.getId(),
                entity.getOwnerId(),
                entity.getBusinessName(),
                entity.getBrandName(),
                entity.getTaxId(),
                entity.getMileageIntervalConfig()
        );
    }
}
