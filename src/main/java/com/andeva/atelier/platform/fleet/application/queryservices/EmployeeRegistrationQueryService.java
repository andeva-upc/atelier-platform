package com.andeva.atelier.platform.fleet.application.queryservices;

import com.andeva.atelier.platform.fleet.domain.model.aggregates.EmployeeRegistration;
import com.andeva.atelier.platform.fleet.domain.model.queries.GetEmployeeRegistrationByIdQuery;

import java.util.Optional;

public interface EmployeeRegistrationQueryService {
    Optional<EmployeeRegistration> handle(GetEmployeeRegistrationByIdQuery query);
}
