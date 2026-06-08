package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Customer;
import com.andeva.atelier.platform.core.domain.model.valueobjects.CustomerId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.UserId;
import com.andeva.atelier.platform.core.domain.repositories.CustomerRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.CustomerPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.CustomerPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.CustomerPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerPersistenceRepository jpaRepository;

    public CustomerRepositoryImpl(CustomerPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerPersistenceEntity entity = null;
        if (customer.getId() != null) {
            entity = jpaRepository.findById(customer.getId().value()).orElse(new CustomerPersistenceEntity());
        } else {
            entity = new CustomerPersistenceEntity();
        }
        
        CustomerPersistenceAssembler.toEntity(customer, entity);
        CustomerPersistenceEntity savedEntity = jpaRepository.save(entity);
        return CustomerPersistenceAssembler.toDomain(savedEntity);
    }

    @Override
    public Optional<Customer> findById(CustomerId id) {
        return jpaRepository.findById(id.value()).map(CustomerPersistenceAssembler::toDomain);
    }

    @Override
    public Optional<Customer> findByUserId(UserId userId) {
        return jpaRepository.findByUserId(userId.value()).map(CustomerPersistenceAssembler::toDomain);
    }

    @Override
    public boolean existsByUserId(UserId userId) {
        return jpaRepository.existsByUserId(userId.value());
    }

    @Override
    public void delete(Customer customer) {
        if (customer.getId() != null) {
            jpaRepository.findById(customer.getId().value()).ifPresent(jpaRepository::delete);
        }
    }
}
