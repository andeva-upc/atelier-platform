package com.andeva.atelier.platform.core.domain.repositories;

import com.andeva.atelier.platform.core.domain.model.aggregates.Employee;
import com.andeva.atelier.platform.core.domain.model.valueobjects.EmployeeId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.UserId;

import java.util.Optional;

public interface EmployeeRepository {
    void save(Employee employee);
    Optional<Employee> findById(EmployeeId id);
    Optional<Employee> findByUserId(UserId userId);
    boolean existsByUserId(UserId userId);
    void delete(Employee employee);
}
