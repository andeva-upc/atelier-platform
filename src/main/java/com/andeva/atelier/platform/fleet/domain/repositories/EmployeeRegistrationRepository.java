package com.andeva.atelier.platform.fleet.domain.repositories;

import com.andeva.atelier.platform.core.domain.model.valueobjects.EmployeeId;
import com.andeva.atelier.platform.fleet.domain.model.aggregates.EmployeeRegistration;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRegistrationRepository {
    EmployeeRegistration save(EmployeeRegistration registration);
    Optional<EmployeeRegistration> findById(EmployeeId id);
    boolean existsByEmployeeIdAndBranchId(UUID employeeId, UUID branchId);
}
