package com.andeva.atelier.platform.fleet.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.EmployeeRegistrationPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.EmployeeRegistrationPersistenceRepository;
import com.andeva.atelier.platform.fleet.domain.model.aggregates.EmployeeRegistration;
import com.andeva.atelier.platform.fleet.domain.repositories.EmployeeRegistrationRepository;
import com.andeva.atelier.platform.fleet.infrastructure.persistence.jpa.assemblers.EmployeeRegistrationPersistenceAssembler;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class EmployeeRegistrationRepositoryAdapter implements EmployeeRegistrationRepository {

    private final EmployeeRegistrationPersistenceRepository persistenceRepository;

    public EmployeeRegistrationRepositoryAdapter(EmployeeRegistrationPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public EmployeeRegistration save(EmployeeRegistration registration) {
        EmployeeRegistrationPersistenceEntity entity;
        if (registration.getId() != null) {
            entity = persistenceRepository.findById(registration.getId().value())
                    .orElseGet(EmployeeRegistrationPersistenceEntity::new);
        } else {
            entity = new EmployeeRegistrationPersistenceEntity();
        }
        EmployeeRegistrationPersistenceAssembler.toEntity(registration, entity);
        return EmployeeRegistrationPersistenceAssembler.toDomain(persistenceRepository.save(entity));
    }

    @Override
    public boolean existsByEmployeeIdAndBranchId(UUID employeeId, UUID branchId) {
        return persistenceRepository.findByEmployeeIdAndBranchId(employeeId, branchId).isPresent();
    }
}