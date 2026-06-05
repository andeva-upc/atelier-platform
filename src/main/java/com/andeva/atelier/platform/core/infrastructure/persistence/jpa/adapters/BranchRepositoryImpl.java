package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Branch;
import com.andeva.atelier.platform.core.domain.repositories.BranchRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.BranchPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.BranchPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.BranchPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class BranchRepositoryImpl implements BranchRepository {

    private final BranchPersistenceRepository jpaRepository;

    public BranchRepositoryImpl(BranchPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Branch branch) {
        BranchPersistenceEntity entity = jpaRepository.findById(branch.getId()).orElse(new BranchPersistenceEntity());
        BranchPersistenceAssembler.toEntity(branch, entity);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Branch> findById(UUID id) {
        return jpaRepository.findById(id).map(BranchPersistenceAssembler::toDomain);
    }

    @Override
    public List<Branch> findAllByWorkshopId(UUID workshopId) {
        return jpaRepository.findAllByWorkshopId(workshopId).stream()
                .map(BranchPersistenceAssembler::toDomain)
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
}
