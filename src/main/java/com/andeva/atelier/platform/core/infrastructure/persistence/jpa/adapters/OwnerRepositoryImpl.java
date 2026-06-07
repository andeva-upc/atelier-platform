package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Owner;
import com.andeva.atelier.platform.core.domain.model.valueobjects.OwnerId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.UserId;
import com.andeva.atelier.platform.core.domain.repositories.OwnerRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.OwnerPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.OwnerPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.OwnerPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OwnerRepositoryImpl implements OwnerRepository {

    private final OwnerPersistenceRepository jpaRepository;

    public OwnerRepositoryImpl(OwnerPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Owner owner) {
        OwnerPersistenceEntity entity = null;
        if (owner.getId() != null) {
            entity = jpaRepository.findById(owner.getId().value()).orElse(new OwnerPersistenceEntity());
        } else {
            entity = new OwnerPersistenceEntity();
        }
        OwnerPersistenceAssembler.toEntity(owner, entity);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Owner> findById(OwnerId id) {
        return jpaRepository.findById(id.value()).map(OwnerPersistenceAssembler::toDomain);
    }

    @Override
    public Optional<Owner> findByUserId(UserId userId) {
        return jpaRepository.findByUserId(userId.value()).map(OwnerPersistenceAssembler::toDomain);
    }

    @Override
    public boolean existsByUserId(UserId userId) {
        return jpaRepository.existsByUserId(userId.value());
    }

    @Override
    public boolean existsById(OwnerId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void delete(Owner owner) {
        if (owner.getId() != null) {
            jpaRepository.findById(owner.getId().value()).ifPresent(jpaRepository::delete);
        }
    }
}
