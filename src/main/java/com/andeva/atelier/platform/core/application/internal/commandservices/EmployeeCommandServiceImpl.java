package com.andeva.atelier.platform.core.application.internal.commandservices;

import com.andeva.atelier.platform.core.application.commandservices.EmployeeCommandService;
import com.andeva.atelier.platform.core.domain.model.aggregates.Employee;
import com.andeva.atelier.platform.core.domain.model.commands.CreateEmployeeCommand;
import com.andeva.atelier.platform.core.domain.model.commands.DeleteEmployeeCommand;
import com.andeva.atelier.platform.core.domain.model.commands.UpdateEmployeeCommand;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.domain.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeCommandServiceImpl implements EmployeeCommandService {

    private final EmployeeRepository employeeRepository;

    public EmployeeCommandServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Optional<Employee> handle(CreateEmployeeCommand command) {
        if (employeeRepository.existsByUserId(command.userId())) {
            throw new IllegalArgumentException("core.error.employee.profileAlreadyExists");
        }

        var document = new Document(command.documentType(), command.documentNumber());
        var personName = new PersonName(command.firstName(), command.lastName());

        var employee = new Employee(
                command.userId(),
                personName,
                document,
                command.phone()
        );

        employeeRepository.save(employee);
        return employeeRepository.findByUserId(command.userId());
    }

    @Override
    public Optional<Employee> handle(UpdateEmployeeCommand command) {
        var result = employeeRepository.findByUserId(command.userId());
        if (result.isEmpty()) throw new IllegalArgumentException("core.error.employee.notFound");

        var employee = result.get();
        
        employee.update(
            command.firstName(),
            command.lastName(),
            command.documentType(),
            command.documentNumber(),
            command.phone()
        );

        employeeRepository.save(employee);
        return Optional.of(employee);
    }

    @Override
    public void handle(DeleteEmployeeCommand command) {
        var existingEmployee = employeeRepository.findByUserId(command.userId());
        if (existingEmployee.isEmpty()) {
            throw new IllegalArgumentException("core.error.employee.notFound");
        }
        
        employeeRepository.delete(existingEmployee.get());
    }
}
