package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.core.domain.model.aggregates.Owner;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.OwnerPersistenceEntity;

public class OwnerPersistenceAssembler {

    public static OwnerPersistenceEntity toEntity(Owner owner, OwnerPersistenceEntity entity) {
        if (entity == null) {
            entity = new OwnerPersistenceEntity();
        }
        entity.setId(owner.getId());
        entity.setUserId(owner.getUserId());
        
        if (owner.getName() != null) {
            entity.setFirstName(owner.getName().firstName());
            entity.setLastName(owner.getName().lastName());
        }
        
        if (owner.getDocument() != null) {
            entity.setDocumentType(owner.getDocument().getDocumentType().name());
            entity.setDocumentNumber(owner.getDocument().getDocumentNumber());
        }
        
        entity.setPhone(owner.getPhone());
        return entity;
    }

    public static Owner toDomain(OwnerPersistenceEntity entity) {
        PersonName personName = new PersonName(entity.getFirstName(), entity.getLastName());
        Document document = new Document(entity.getDocumentType(), entity.getDocumentNumber());

        var owner = new Owner(
                entity.getUserId(),
                personName,
                document,
                entity.getPhone()
        );
        
        try {
            var field = com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(owner, entity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Could not set ID on domain object", e);
        }
        return owner;
    }
}
