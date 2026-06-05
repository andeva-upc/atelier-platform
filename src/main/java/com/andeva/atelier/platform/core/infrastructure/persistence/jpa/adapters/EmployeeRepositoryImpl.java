package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Employee;
import com.andeva.atelier.platform.core.domain.repositories.EmployeeRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.EmployeePersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.EmployeePersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.EmployeePersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeePersistenceRepository jpaRepository;

    public EmployeeRepositoryImpl(EmployeePersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Employee employee) {
        EmployeePersistenceEntity entity = jpaRepository.findById(employee.getId()).orElse(new EmployeePersistenceEntity());
        EmployeePersistenceAssembler.toEntity(employee, entity);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Employee> findById(UUID id) {
        return jpaRepository.findById(id).map(EmployeePersistenceAssembler::toDomain);
    }

    @Override
    public Optional<Employee> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).map(EmployeePersistenceAssembler::toDomain);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public void delete(Employee employee) {
        jpaRepository.findById(employee.getId()).ifPresent(jpaRepository::delete);
    }
}
