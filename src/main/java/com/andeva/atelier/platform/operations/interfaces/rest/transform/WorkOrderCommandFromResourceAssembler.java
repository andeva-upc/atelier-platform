package com.andeva.atelier.platform.operations.interfaces.rest.transform;

import com.andeva.atelier.platform.operations.domain.model.commands.*;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import com.andeva.atelier.platform.operations.interfaces.rest.resources.*;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.*;

import java.util.UUID;

/**
 * Assembler class to map incoming HTTP resources into domain-level Commands.
 */
public final class WorkOrderCommandFromResourceAssembler {

    private WorkOrderCommandFromResourceAssembler() {}

    /**
     * Convierte el recurso de creación en un comando CreateWorkOrderCommand.
     */
    public static CreateWorkOrderCommand toCommandFromResource(CreateWorkOrderResource resource) {
        return new CreateWorkOrderCommand(
                new AppointmentId(resource.appointmentId()),
                new BranchId(resource.branchId()),
                new VehicleId(resource.vehicleId()),
                new CustomerId(resource.customerId()),
                resource.internalNumber(),
                new DiagnosticSummary(resource.diagnosticSummary()),
                new Mileage(resource.mileageIn())
        );
    }

    /**
     * Convierte el recurso de agregar tarea en un comando AddTaskToWorkOrderCommand.
     */
    public static AddTaskToWorkOrderCommand toCommandFromResource(UUID workOrderId, AddTaskResource resource) {
        return new AddTaskToWorkOrderCommand(
                workOrderId,
                new ServiceId(resource.serviceId()),
                new MechanicId(resource.assignedMechanicId()),
                new TaskDescription(resource.description()),
                new Money(resource.laborPrice())
        );
    }

    /**
     * Convierte el recurso de agregar producto en un comando AddProductToTaskCommand.
     */
    public static AddProductToTaskCommand toCommandFromResource(UUID workOrderId, UUID taskId, AddProductResource resource) {
        return new AddProductToTaskCommand(
                workOrderId,
                taskId,
                new ProductId(resource.productId()),
                new Quantity(resource.quantity()),
                new Money(resource.unitPrice())
        );
    }
}