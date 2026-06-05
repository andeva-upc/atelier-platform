package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.core.domain.model.aggregates.Employee;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.EmployeePersistenceEntity;

public class EmployeePersistenceAssembler {

    public static EmployeePersistenceEntity toEntity(Employee employee, EmployeePersistenceEntity entity) {
        if (entity == null) {
            entity = new EmployeePersistenceEntity();
        }
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
        return entity;
    }

    public static Employee toDomain(EmployeePersistenceEntity entity) {
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
