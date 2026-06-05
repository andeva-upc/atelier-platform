package com.andeva.atelier.platform.core.interfaces.rest;

import com.andeva.atelier.platform.core.domain.model.queries.GetEmployeeByIdQuery;
import com.andeva.atelier.platform.core.application.commandservices.EmployeeCommandService;
import com.andeva.atelier.platform.core.application.queryservices.EmployeeQueryService;
import com.andeva.atelier.platform.core.domain.model.commands.DeleteEmployeeCommand;
import com.andeva.atelier.platform.core.interfaces.rest.resources.CreateEmployeeResource;
import com.andeva.atelier.platform.core.interfaces.rest.resources.EmployeeResource;
import com.andeva.atelier.platform.core.interfaces.rest.resources.UpdateEmployeeResource;
import com.andeva.atelier.platform.core.interfaces.rest.transform.CreateEmployeeCommandFromResourceAssembler;
import com.andeva.atelier.platform.core.interfaces.rest.transform.EmployeeResourceFromEntityAssembler;
import com.andeva.atelier.platform.core.interfaces.rest.transform.UpdateEmployeeCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/employees", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Employees", description = "Employee Management Endpoints")
public class EmployeesController {

    private final EmployeeCommandService employeeCommandService;
    private final EmployeeQueryService employeeQueryService;

    public EmployeesController(EmployeeCommandService employeeCommandService, EmployeeQueryService employeeQueryService) {
        this.employeeCommandService = employeeCommandService;
        this.employeeQueryService = employeeQueryService;
    }

    @Operation(summary = "Create a new employee profile", description = "Creates a new employee profile associated with a user ID")
    @PostMapping
    public ResponseEntity<EmployeeResource> createEmployee(@RequestBody CreateEmployeeResource resource) {
        var command = CreateEmployeeCommandFromResourceAssembler.toCommandFromResource(resource);
        var employee = employeeCommandService.handle(command);
        if (employee.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var employeeResource = EmployeeResourceFromEntityAssembler.toResourceFromEntity(employee.get());
        return new ResponseEntity<>(employeeResource, HttpStatus.CREATED);
    }

    @Operation(summary = "Update an employee profile", description = "Updates an existing employee profile using the user ID")
    @PutMapping("/user/{userId}")
    public ResponseEntity<EmployeeResource> updateEmployee(@PathVariable UUID userId, @RequestBody UpdateEmployeeResource resource) {
        var command = UpdateEmployeeCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var employee = employeeCommandService.handle(command);
        if (employee.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var employeeResource = EmployeeResourceFromEntityAssembler.toResourceFromEntity(employee.get());
        return ResponseEntity.ok(employeeResource);
    }

    @Operation(summary = "Get an employee profile by ID", description = "Retrieves the details of a specific employee profile")
    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResource> getEmployeeById(@PathVariable UUID employeeId) {
        var query = new GetEmployeeByIdQuery(employeeId);
        var employee = employeeQueryService.handle(query);
        if (employee.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var employeeResource = EmployeeResourceFromEntityAssembler.toResourceFromEntity(employee.get());
        return ResponseEntity.ok(employeeResource);
    }

    @Operation(summary = "Delete an employee profile", description = "Deletes an existing employee profile using the user ID")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable UUID userId) {
        var command = new DeleteEmployeeCommand(userId);
        employeeCommandService.handle(command);
        return ResponseEntity.ok().build();
    }
}
