package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.core.domain.model.aggregates.Customer;
import com.andeva.atelier.platform.core.domain.repositories.CustomerRepository;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.assemblers.CustomerPersistenceAssembler;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities.CustomerPersistenceEntity;
import com.andeva.atelier.platform.core.infrastructure.persistence.jpa.repositories.CustomerPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerPersistenceRepository jpaRepository;

    public CustomerRepositoryImpl(CustomerPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Customer customer) {
        CustomerPersistenceEntity entity = jpaRepository.findById(customer.getId()).orElse(new CustomerPersistenceEntity());
        CustomerPersistenceAssembler.toEntity(customer, entity);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpaRepository.findById(id).map(CustomerPersistenceAssembler::toDomain);
    }

    @Override
    public Optional<Customer> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).map(CustomerPersistenceAssembler::toDomain);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public void delete(Customer customer) {
        jpaRepository.findById(customer.getId()).ifPresent(jpaRepository::delete);
    }
}
