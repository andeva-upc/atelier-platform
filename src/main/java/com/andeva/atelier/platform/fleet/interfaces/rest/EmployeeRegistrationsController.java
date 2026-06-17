package com.andeva.atelier.platform.fleet.interfaces.rest;

import com.andeva.atelier.platform.core.domain.model.valueobjects.EmployeeId;
import com.andeva.atelier.platform.fleet.application.commandservices.EmployeeRegistrationCommandFailure;
import com.andeva.atelier.platform.fleet.application.commandservices.EmployeeRegistrationCommandService;
import com.andeva.atelier.platform.fleet.application.queryservices.EmployeeRegistrationQueryService;
import com.andeva.atelier.platform.fleet.domain.model.queries.GetEmployeeRegistrationByIdQuery;
import com.andeva.atelier.platform.fleet.interfaces.rest.resources.CreateEmployeeRegistrationResource;
import com.andeva.atelier.platform.fleet.interfaces.rest.transform.CreateEmployeeRegistrationCommandFromResourceAssembler;
import com.andeva.atelier.platform.fleet.interfaces.rest.transform.EmployeeRegistrationResourceFromAggregateAssembler;
import com.andeva.atelier.platform.shared.application.result.ApplicationError;
import com.andeva.atelier.platform.shared.interfaces.rest.transform.ErrorResponseAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/employee-registrations", produces = "application/json")
@Tag(name = "EmployeeRegistrations", description = "Employee Registration Management Endpoints")
public class EmployeeRegistrationsController {

    private final EmployeeRegistrationCommandService commandService;
    private final EmployeeRegistrationQueryService queryService;
    private final MessageSource messageSource;

    public EmployeeRegistrationsController(EmployeeRegistrationCommandService commandService,
                                           EmployeeRegistrationQueryService queryService,
                                           MessageSource messageSource) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.messageSource = messageSource;
    }

    @PostMapping
    @Operation(summary = "Create a new employee registration", description = "Creates a new employee registration")
    public ResponseEntity<?> create(@Valid @RequestBody CreateEmployeeRegistrationResource resource) {
        var command = CreateEmployeeRegistrationCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return result.fold(
                registration -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(EmployeeRegistrationResourceFromAggregateAssembler.toResourceFromAggregate(registration)),
                this::handleCommandFailure
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an employee registration by ID", description = "Retrieves an employee registration by ID")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        var query = new GetEmployeeRegistrationByIdQuery(new EmployeeId(id));
        var registration = queryService.handle(query);
        if (registration.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = EmployeeRegistrationResourceFromAggregateAssembler.toResourceFromAggregate(registration.get());
        return ResponseEntity.ok(resource);
    }

    private ResponseEntity<?> handleCommandFailure(EmployeeRegistrationCommandFailure failure) {
        String messageKey = switch (failure) {
            case REGISTRATION_ALREADY_EXISTS -> "fleet.error.employeeRegistration.alreadyExists";
            case REGISTRATION_NOT_FOUND -> "fleet.error.employeeRegistration.notFound";
            case INVALID_REGISTRATION_DATA -> "fleet.error.employeeRegistration.invalidData";
        };
        String message = messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
        ApplicationError error = switch (failure) {
            case REGISTRATION_ALREADY_EXISTS -> ApplicationError.conflict("employeeRegistration", message);
            case REGISTRATION_NOT_FOUND -> ApplicationError.notFound("employeeRegistration", message);
            case INVALID_REGISTRATION_DATA -> ApplicationError.validationError("employeeRegistration", message);
        };
        return ErrorResponseAssembler.toErrorResponseFromApplicationError(error);
    }
}
