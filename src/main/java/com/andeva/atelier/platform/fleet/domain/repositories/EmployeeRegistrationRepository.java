package com.andeva.atelier.platform.fleet.domain.repositories;

import com.andeva.atelier.platform.fleet.domain.model.aggregates.EmployeeRegistration;

import java.util.UUID;

public interface EmployeeRegistrationRepository {
    EmployeeRegistration save(EmployeeRegistration registration);
    boolean existsByEmployeeIdAndBranchId(UUID employeeId, UUID branchId);
}
