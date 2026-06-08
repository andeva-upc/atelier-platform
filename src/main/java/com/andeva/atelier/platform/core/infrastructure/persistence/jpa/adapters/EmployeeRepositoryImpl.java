package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Employee;
import com.andeva.atelier.platform.core.domain.model.valueobjects.EmployeeId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.UserId;
import com.andeva.atelier.platform.core.domain.repositories.EmployeeRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.EmployeePersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.EmployeePersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.EmployeePersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeePersistenceRepository jpaRepository;

    public EmployeeRepositoryImpl(EmployeePersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Employee save(Employee employee) {
        EmployeePersistenceEntity entity = null;
        if (employee.getId() != null) {
            entity = jpaRepository.findById(employee.getId().value()).orElse(new EmployeePersistenceEntity());
        } else {
            entity = new EmployeePersistenceEntity();
        }
        
        EmployeePersistenceAssembler.toEntity(employee, entity);
        EmployeePersistenceEntity savedEntity = jpaRepository.save(entity);
        return EmployeePersistenceAssembler.toDomain(savedEntity);
    }

    @Override
    public Optional<Employee> findById(EmployeeId id) {
        return jpaRepository.findById(id.value()).map(EmployeePersistenceAssembler::toDomain);
    }

    @Override
    public Optional<Employee> findByUserId(UserId userId) {
        return jpaRepository.findByUserId(userId.value()).map(EmployeePersistenceAssembler::toDomain);
    }

    @Override
    public boolean existsByUserId(UserId userId) {
        return jpaRepository.existsByUserId(userId.value());
    }

    @Override
    public void delete(Employee employee) {
        if (employee.getId() != null) {
            jpaRepository.findById(employee.getId().value()).ifPresent(jpaRepository::delete);
        }
    }
}
