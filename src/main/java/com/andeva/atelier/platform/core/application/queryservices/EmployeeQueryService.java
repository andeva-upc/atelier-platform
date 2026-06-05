package com.andeva.atelier.platform.core.application.queryservices;

import com.andeva.atelier.platform.core.domain.model.aggregates.Employee;
import com.andeva.atelier.platform.core.domain.model.queries.GetEmployeeByIdQuery;

import java.util.Optional;

public interface EmployeeQueryService {
    Optional<Employee> handle(GetEmployeeByIdQuery query);
}
