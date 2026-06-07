package com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Quote;
import com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.entities.QuotePersistenceEntity;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;

public class QuotePersistenceAssembler {

    public static QuotePersistenceEntity toEntity(Quote aggregate) {
        if (aggregate == null) return null;
        var entity = new QuotePersistenceEntity();
        entity.setId(aggregate.getId());
        entity.setWorkOrderId(aggregate.getWorkOrderId());
        entity.setBranchId(aggregate.getBranchId().value());
        entity.setSubtotalAmount(aggregate.getSubtotalAmount().amount());
        entity.setDiscountPercentage(aggregate.getDiscountPercentage());
        entity.setTotalAmount(aggregate.getTotalAmount().amount());
        entity.setStatus(aggregate.getStatus());
        return entity;
    }

    public static Quote toAggregate(QuotePersistenceEntity entity) {
        if (entity == null) return null;
        return new Quote(
                entity.getId(),
                entity.getWorkOrderId(),
                new BranchId(entity.getBranchId()),
                new Money(entity.getSubtotalAmount()),
                entity.getDiscountPercentage(),
                new Money(entity.getTotalAmount()),
                entity.getStatus()
        );
    }
}
