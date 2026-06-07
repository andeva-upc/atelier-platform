package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Workshop;
import com.andeva.atelier.platform.core.domain.model.valueobjects.OwnerId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.WorkshopId;
import com.andeva.atelier.platform.core.domain.repositories.WorkshopRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.WorkshopPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.WorkshopPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.WorkshopPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class WorkshopRepositoryImpl implements WorkshopRepository {

    private final WorkshopPersistenceRepository jpaRepository;

    public WorkshopRepositoryImpl(WorkshopPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Workshop workshop) {
        WorkshopPersistenceEntity entity = null;
        if (workshop.getId() != null) {
            entity = jpaRepository.findById(workshop.getId().value()).orElse(new WorkshopPersistenceEntity());
        } else {
            entity = new WorkshopPersistenceEntity();
        }
        WorkshopPersistenceAssembler.toEntity(workshop, entity);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Workshop> findById(WorkshopId id) {
        return jpaRepository.findById(id.value()).map(WorkshopPersistenceAssembler::toDomain);
    }

    @Override
    public List<Workshop> findAllByOwnerId(OwnerId ownerId) {
        return jpaRepository.findAllByOwnerId(ownerId.value()).stream()
                .map(WorkshopPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(WorkshopId id) {
        return jpaRepository.existsById(id.value());
    }
}
