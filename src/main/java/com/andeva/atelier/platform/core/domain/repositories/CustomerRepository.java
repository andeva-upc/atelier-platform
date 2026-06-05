package com.andeva.atelier.platform.core.domain.repositories;

import com.andeva.atelier.platform.core.domain.model.aggregates.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    void save(Customer customer);
    Optional<Customer> findById(UUID id);
    Optional<Customer> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
    void delete(Customer customer);
}
