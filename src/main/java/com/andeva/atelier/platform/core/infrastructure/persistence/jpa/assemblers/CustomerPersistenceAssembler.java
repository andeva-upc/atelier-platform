package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.core.domain.model.aggregates.Customer;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.CustomerPersistenceEntity;

public class CustomerPersistenceAssembler {

    public static CustomerPersistenceEntity toEntity(Customer customer, CustomerPersistenceEntity entity) {
        if (entity == null) {
            entity = new CustomerPersistenceEntity();
        }
        entity.setId(customer.getId());
        entity.setUserId(customer.getUserId());
        entity.setCorporate(customer.isCorporate());
        
        if (customer.getName() != null) {
            entity.setFirstName(customer.getName().firstName());
            entity.setLastName(customer.getName().lastName());
        }
        
        entity.setBusinessName(customer.getBusinessName());
        
        if (customer.getDocument() != null) {
            entity.setDocumentType(customer.getDocument().getDocumentType().name());
            entity.setDocumentNumber(customer.getDocument().getDocumentNumber());
        }
        
        entity.setPhone(customer.getPhone());
        return entity;
    }

    public static Customer toDomain(CustomerPersistenceEntity entity) {
        PersonName personName = null;
        if (entity.getFirstName() != null && entity.getLastName() != null) {
            personName = new PersonName(entity.getFirstName(), entity.getLastName());
        }
        
        Document document = null;
        if (entity.getDocumentType() != null && entity.getDocumentNumber() != null) {
            document = new Document(entity.getDocumentType(), entity.getDocumentNumber());
        }

        var customer = new Customer(
                entity.getUserId(),
                entity.isCorporate(),
                personName,
                entity.getBusinessName(),
                document,
                entity.getPhone()
        );
        // Force the ID to match what was saved in the DB
                customer.setId(entity.getId());
        return customer;
    }
}
