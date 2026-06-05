package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Owner;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.domain.repositories.OwnerRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.OwnerPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.OwnerJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class OwnerRepositoryImpl implements OwnerRepository {

    private final OwnerJpaRepository jpaRepository;

    public OwnerRepositoryImpl(OwnerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Owner owner) {
        OwnerPersistenceEntity entity = jpaRepository.findById(owner.getId()).orElse(new OwnerPersistenceEntity());
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

        jpaRepository.save(entity);
    }

    @Override
    public Optional<Owner> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Owner> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).map(this::toDomain);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public void delete(Owner owner) {
        jpaRepository.findById(owner.getId()).ifPresent(jpaRepository::delete);
    }

    private Owner toDomain(OwnerPersistenceEntity entity) {
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
