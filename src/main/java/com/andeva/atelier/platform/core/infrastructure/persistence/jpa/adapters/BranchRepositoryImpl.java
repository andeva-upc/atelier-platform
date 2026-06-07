package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Branch;
import com.andeva.atelier.platform.core.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.WorkshopId;
import com.andeva.atelier.platform.core.domain.repositories.BranchRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.BranchPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.BranchPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.BranchPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BranchRepositoryImpl implements BranchRepository {

    private final BranchPersistenceRepository jpaRepository;

    public BranchRepositoryImpl(BranchPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Branch branch) {
        BranchPersistenceEntity entity = null;
        if (branch.getId() != null) {
            entity = jpaRepository.findById(branch.getId().value()).orElse(new BranchPersistenceEntity());
        } else {
            entity = new BranchPersistenceEntity();
        }
        BranchPersistenceAssembler.toEntity(branch, entity);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Branch> findById(BranchId id) {
        return jpaRepository.findById(id.value()).map(BranchPersistenceAssembler::toDomain);
    }

    @Override
    public List<Branch> findAllByWorkshopId(WorkshopId workshopId) {
        return jpaRepository.findAllByWorkshopId(workshopId.value()).stream()
                .map(BranchPersistenceAssembler::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(BranchId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}
