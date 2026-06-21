package com.andeva.atelier.platform.core.application.internal.queryservices;

import com.andeva.atelier.platform.core.application.queryservices.ProfileQueryService;
import com.andeva.atelier.platform.core.domain.model.queries.GetProfileRolesByUserIdQuery;
import com.andeva.atelier.platform.core.domain.repositories.CustomerRepository;
import com.andeva.atelier.platform.core.domain.repositories.EmployeeRepository;
import com.andeva.atelier.platform.core.domain.repositories.OwnerRepository;
import org.springframework.stereotype.Service;

import com.andeva.atelier.platform.core.domain.model.queries.GetProfileByDocumentNumberQuery;
import com.andeva.atelier.platform.core.domain.model.queries.responses.ProfileSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<ProfileSummary> handle(GetProfileByDocumentNumberQuery query) {
        var customer = customerRepository.findByDocumentNumber(query.documentNumber());
        if (customer.isPresent()) {
            var c = customer.get();
            return Optional.of(new ProfileSummary(c.getUserId().value(), c.getFirstName(), c.getLastName(), c.getDocument().getDocumentType(), c.getDocument().getDocumentNumber(), "CUSTOMER"));
        }

        var employee = employeeRepository.findByDocumentNumber(query.documentNumber());
        if (employee.isPresent()) {
            var e = employee.get();
            return Optional.of(new ProfileSummary(e.getUserId().value(), e.getFirstName(), e.getLastName(), e.getDocument().getDocumentType(), e.getDocument().getDocumentNumber(), "EMPLOYEE"));
        }

        var owner = ownerRepository.findByDocumentNumber(query.documentNumber());
        if (owner.isPresent()) {
            var o = owner.get();
            return Optional.of(new ProfileSummary(o.getUserId().value(), o.getFirstName(), o.getLastName(), o.getDocument().getDocumentType(), o.getDocument().getDocumentNumber(), "OWNER"));
        }

        return Optional.empty();
    }
}
