package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Workshop;
import com.andeva.atelier.platform.core.domain.repositories.WorkshopRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.WorkshopPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.WorkshopJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WorkshopRepositoryImpl implements WorkshopRepository {

    private final WorkshopJpaRepository jpaRepository;

    public WorkshopRepositoryImpl(WorkshopJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Workshop workshop) {
        WorkshopPersistenceEntity entity = jpaRepository.findById(workshop.getId()).orElse(new WorkshopPersistenceEntity());
        entity.setId(workshop.getId());
        entity.setOwnerId(workshop.getOwnerId());
        entity.setBusinessName(workshop.getBusinessName());
        entity.setBrandName(workshop.getBrandName());
        entity.setTaxId(workshop.getTaxId());
        entity.setMileageIntervalConfig(workshop.getMileageIntervalConfig());

        jpaRepository.save(entity);
    }

    @Override
    public Optional<Workshop> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Workshop> findAllByOwnerId(UUID ownerId) {
        return jpaRepository.findAllByOwnerId(ownerId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    private Workshop toDomain(WorkshopPersistenceEntity entity) {
        var workshop = new Workshop(
                entity.getOwnerId(),
                entity.getBusinessName(),
                entity.getBrandName(),
                entity.getTaxId(),
                entity.getMileageIntervalConfig()
        );

        try {
            var field = com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(workshop, entity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Could not set ID on domain object", e);
        }
        return workshop;
    }
}
