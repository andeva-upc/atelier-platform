package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Customer;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.domain.repositories.CustomerRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.CustomerPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.CustomerJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;

    public CustomerRepositoryImpl(CustomerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Customer customer) {
        CustomerPersistenceEntity entity = jpaRepository.findById(customer.getId()).orElse(new CustomerPersistenceEntity());
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

        jpaRepository.save(entity);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Customer> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).map(this::toDomain);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public void delete(Customer customer) {
        jpaRepository.findById(customer.getId()).ifPresent(jpaRepository::delete);
    }

    private Customer toDomain(CustomerPersistenceEntity entity) {
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
        try {
            var field = com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(customer, entity.getId());
        } catch (Exception e) {
            throw new RuntimeException("Could not set ID on domain object", e);
        }
        return customer;
    }
}
