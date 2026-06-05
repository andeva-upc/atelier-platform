package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Workshop;
import com.andeva.atelier.platform.core.domain.repositories.WorkshopRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.WorkshopPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.WorkshopPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.WorkshopPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WorkshopRepositoryImpl implements WorkshopRepository {

    private final WorkshopPersistenceRepository jpaRepository;

    public WorkshopRepositoryImpl(WorkshopPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Workshop workshop) {
        WorkshopPersistenceEntity entity = jpaRepository.findById(workshop.getId()).orElse(new WorkshopPersistenceEntity());
        WorkshopPersistenceAssembler.toEntity(workshop, entity);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Workshop> findById(UUID id) {
        return jpaRepository.findById(id).map(WorkshopPersistenceAssembler::toDomain);
    }

    @Override
    public List<Workshop> findAllByOwnerId(UUID ownerId) {
        return jpaRepository.findAllByOwnerId(ownerId).stream()
                .map(WorkshopPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
}
