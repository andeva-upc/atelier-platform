package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Owner;
import com.andeva.atelier.platform.core.domain.repositories.OwnerRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.OwnerPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.OwnerPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.OwnerPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class OwnerRepositoryImpl implements OwnerRepository {

    private final OwnerPersistenceRepository jpaRepository;

    public OwnerRepositoryImpl(OwnerPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Owner owner) {
        OwnerPersistenceEntity entity = jpaRepository.findById(owner.getId()).orElse(new OwnerPersistenceEntity());
        OwnerPersistenceAssembler.toEntity(owner, entity);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Owner> findById(UUID id) {
        return jpaRepository.findById(id).map(OwnerPersistenceAssembler::toDomain);
    }

    @Override
    public Optional<Owner> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).map(OwnerPersistenceAssembler::toDomain);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public void delete(Owner owner) {
        jpaRepository.findById(owner.getId()).ifPresent(jpaRepository::delete);
    }
}
