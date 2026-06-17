package com.andeva.atelier.platform.fleet.application.internal.queryservices;

import com.andeva.atelier.platform.fleet.application.queryservices.EmployeeRegistrationQueryService;
import com.andeva.atelier.platform.fleet.domain.model.aggregates.EmployeeRegistration;
import com.andeva.atelier.platform.fleet.domain.model.queries.GetEmployeeRegistrationByIdQuery;
import com.andeva.atelier.platform.fleet.domain.repositories.EmployeeRegistrationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeRegistrationQueryServiceImpl implements EmployeeRegistrationQueryService {

    private final EmployeeRegistrationRepository employeeRegistrationRepository;

    public EmployeeRegistrationQueryServiceImpl(EmployeeRegistrationRepository employeeRegistrationRepository) {
        this.employeeRegistrationRepository = employeeRegistrationRepository;
    }

    @Override
    public Optional<EmployeeRegistration> handle(GetEmployeeRegistrationByIdQuery query) {
        return employeeRegistrationRepository.findById(query.id());
    }
}
