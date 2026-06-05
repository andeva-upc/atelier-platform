package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Branch;
import com.andeva.atelier.platform.core.domain.repositories.BranchRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.BranchPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.BranchJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BranchRepositoryImpl implements BranchRepository {

    private final BranchJpaRepository jpaRepository;

    public BranchRepositoryImpl(BranchJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Branch branch) {
        BranchPersistenceEntity entity = jpaRepository.findById(branch.getId()).orElse(new BranchPersistenceEntity());
        entity.setId(branch.getId());
        entity.setWorkshopId(branch.getWorkshopId());
        entity.setCode(branch.getCode());
        entity.setName(branch.getName());
        if (branch.getAddress() != null) {
            entity.setAddress(branch.getAddress().value());
        }
        entity.setPhone(branch.getPhone());

        jpaRepository.save(entity);
    }

    @Override
    public Optional<Branch> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Branch> findAllByWorkshopId(UUID workshopId) {
        return jpaRepository.findAllByWorkshopId(workshopId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    private Branch toDomain(BranchPersistenceEntity entity) {
        var branch = new Branch(
                entity.getWorkshopId(),
                entity.getCode(),
                entity.getName(),
                entity.getAddress(),
                entity.getPhone()
        );

        try {
            var field = com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(branch, entity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Could not set ID on domain object", e);
        }
        return branch;
    }
}
