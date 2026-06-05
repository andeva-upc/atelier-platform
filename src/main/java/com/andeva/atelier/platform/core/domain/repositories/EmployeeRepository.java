package com.andeva.atelier.platform.core.domain.repositories;

import com.andeva.atelier.platform.core.domain.model.aggregates.Employee;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {
    void save(Employee employee);
    Optional<Employee> findById(UUID id);
    Optional<Employee> findByUserId(UUID userId);
    boolean existsByUserId(UUID userId);
    void delete(Employee employee);
}
