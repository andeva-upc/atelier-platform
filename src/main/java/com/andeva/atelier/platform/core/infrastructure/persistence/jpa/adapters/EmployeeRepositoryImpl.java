package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Employee;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.domain.repositories.EmployeeRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.EmployeePersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.EmployeeJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeJpaRepository jpaRepository;

    public EmployeeRepositoryImpl(EmployeeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Employee employee) {
        EmployeePersistenceEntity entity = jpaRepository.findById(employee.getId()).orElse(new EmployeePersistenceEntity());
        entity.setId(employee.getId());
        entity.setUserId(employee.getUserId());
        
        if (employee.getName() != null) {
            entity.setFirstName(employee.getName().firstName());
            entity.setLastName(employee.getName().lastName());
        }
        
        if (employee.getDocument() != null) {
            entity.setDocumentType(employee.getDocument().getDocumentType().name());
            entity.setDocumentNumber(employee.getDocument().getDocumentNumber());
        }
        
        entity.setPhone(employee.getPhone());

        jpaRepository.save(entity);
    }

    @Override
    public Optional<Employee> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Employee> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).map(this::toDomain);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public void delete(Employee employee) {
        jpaRepository.findById(employee.getId()).ifPresent(jpaRepository::delete);
    }

    private Employee toDomain(EmployeePersistenceEntity entity) {
        PersonName personName = new PersonName(entity.getFirstName(), entity.getLastName());
        Document document = new Document(entity.getDocumentType(), entity.getDocumentNumber());

        var employee = new Employee(
                entity.getUserId(),
                personName,
                document,
                entity.getPhone()
        );
        
        try {
            var field = com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(employee, entity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Could not set ID on domain object", e);
        }
        return employee;
    }
}
