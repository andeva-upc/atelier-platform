package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.core.domain.model.aggregates.Employee;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.EmployeeId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Phone;
import com.andeva.atelier.platform.core.domain.model.valueobjects.UserId;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.EmployeePersistenceEntity;

public class EmployeePersistenceAssembler {

    public static EmployeePersistenceEntity toEntity(Employee employee, EmployeePersistenceEntity entity) {
        if (entity == null) {
            entity = new EmployeePersistenceEntity();
        }
        entity.setId(employee.getId() != null ? employee.getId().value() : null);
        entity.setUserId(employee.getUserId() != null ? employee.getUserId().value() : null);
        
        if (employee.getName() != null) {
            entity.setFirstName(employee.getName().firstName());
            entity.setLastName(employee.getName().lastName());
        }
        
        if (employee.getDocument() != null) {
            entity.setDocumentType(employee.getDocument().getDocumentType().name());
            entity.setDocumentNumber(employee.getDocument().getDocumentNumber());
        }
        
        entity.setPhone(employee.getPhone() != null ? employee.getPhone().value() : null);
        return entity;
    }

    public static Employee toDomain(EmployeePersistenceEntity entity) {
        PersonName personName = new PersonName(entity.getFirstName(), entity.getLastName());
        Document document = new Document(entity.getDocumentType(), entity.getDocumentNumber());

        return new Employee(
                new EmployeeId(entity.getId()),
                new UserId(entity.getUserId()),
                personName,
                document,
                new Phone(entity.getPhone())
        );
    }
}
