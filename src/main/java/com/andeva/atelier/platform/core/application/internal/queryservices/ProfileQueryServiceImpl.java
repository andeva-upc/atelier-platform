package com.andeva.atelier.platform.core.application.internal.queryservices;

import com.andeva.atelier.platform.core.application.queryservices.ProfileQueryService;
import com.andeva.atelier.platform.core.domain.model.queries.GetProfileRolesByUserIdQuery;
import com.andeva.atelier.platform.core.domain.repositories.CustomerRepository;
import com.andeva.atelier.platform.core.domain.repositories.EmployeeRepository;
import com.andeva.atelier.platform.core.domain.repositories.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;
    private final EmployeeRepository employeeRepository;

    public ProfileQueryServiceImpl(
            CustomerRepository customerRepository,
            OwnerRepository ownerRepository,
            EmployeeRepository employeeRepository) {
        this.customerRepository = customerRepository;
        this.ownerRepository = ownerRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<String> handle(GetProfileRolesByUserIdQuery query) {
        List<String> roles = new ArrayList<>();

        if (customerRepository.existsByUserId(query.userId())) {
            roles.add("CUSTOMER");
        }
        if (ownerRepository.existsByUserId(query.userId())) {
            roles.add("OWNER");
        }
        if (employeeRepository.existsByUserId(query.userId())) {
            roles.add("EMPLOYEE");
        }

        return roles;
    }
}
