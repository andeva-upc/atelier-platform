package com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrderTaskProduct;
import com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.entities.WorkOrderTaskProductPersistenceEntity;

public class WorkOrderTaskProductPersistenceAssembler {

    public static WorkOrderTaskProductPersistenceEntity toPersistenceEntity(WorkOrderTaskProduct domainEntity) {
        if (domainEntity == null) return null;
        WorkOrderTaskProductPersistenceEntity persistenceEntity = new WorkOrderTaskProductPersistenceEntity();
        if (domainEntity.getVersion() != null) {
            persistenceEntity.setId(domainEntity.getId());
        }
        persistenceEntity.setProductId(domainEntity.getProductId());
        persistenceEntity.setBranchId(domainEntity.getBranchId());
        persistenceEntity.setQuantity(domainEntity.getQuantity());
        persistenceEntity.setUnitPrice(domainEntity.getUnitPrice());
        persistenceEntity.setTotalAmount(domainEntity.getTotalAmount());
        persistenceEntity.setCreatedAt(domainEntity.getCreatedAt());
        persistenceEntity.setUpdatedAt(domainEntity.getUpdatedAt());
        persistenceEntity.setDeletedAt(domainEntity.getDeletedAt());
        persistenceEntity.setVersion(domainEntity.getVersion());
        return persistenceEntity;
    }

    public static WorkOrderTaskProduct toDomainEntity(WorkOrderTaskProductPersistenceEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        return new WorkOrderTaskProduct(
                persistenceEntity.getId(),
                persistenceEntity.getProductId(),
                persistenceEntity.getBranchId(),
                persistenceEntity.getQuantity(),
                persistenceEntity.getUnitPrice(),
                persistenceEntity.getTotalAmount(),
                persistenceEntity.getCreatedAt(),
                persistenceEntity.getUpdatedAt(),
                persistenceEntity.getDeletedAt(),
                persistenceEntity.getVersion()
        );
    }
}
