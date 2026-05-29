package com.andeva.atelier.platform.operations.application.internal.commandservices;

import com.andeva.atelier.platform.operations.application.commandservices.WorkOrderCommandFailure;
import com.andeva.atelier.platform.operations.application.commandservices.WorkOrderCommandService;
import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrder;
import com.andeva.atelier.platform.operations.domain.model.commands.*;
import com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.WorkOrderRepository;
import com.andeva.atelier.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Internal application service implementing {@link WorkOrderCommandService}.
 * Orchestrates use cases using the domain model and work order repository inside atomic transactions.
 */
@Service
public class WorkOrderCommandServiceImpl implements WorkOrderCommandService {

    private final WorkOrderRepository workOrderRepository;

    // Inyección de dependencias por constructor
    public WorkOrderCommandServiceImpl(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    @Transactional
    public Result<WorkOrder, WorkOrderCommandFailure> handle(CreateWorkOrderCommand command) {
        try {
            // INVARIANTE: Validamos que no exista otra orden para la misma cita (Appointment)
            if (workOrderRepository.existsByAppointmentId(command.appointmentId())) {
                return Result.failure(new WorkOrderCommandFailure.Duplicate(
                        "operations.error.workOrder.alreadyExistsForAppointment"));
            }

            // Instanciamos el Agregado de forma limpia
            WorkOrder workOrder = new WorkOrder(
                    command.appointmentId(),
                    command.branchId(),
                    command.vehicleId(),
                    command.customerId(),
                    command.internalNumber(),
                    command.diagnosticSummary(),
                    command.mileageIn()
            );

            workOrderRepository.save(workOrder);
            return Result.success(workOrder);

        } catch (IllegalArgumentException e) {
            return Result.failure(new WorkOrderCommandFailure.NotFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(new WorkOrderCommandFailure.InvalidState(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<WorkOrder, WorkOrderCommandFailure> handle(AddTaskToWorkOrderCommand command) {
        try {
            WorkOrder workOrder = findWorkOrderOrThrow(command.workOrderId());
            workOrder.addTask(command.serviceId(), command.mechanicId(), command.description(), command.laborPrice());
            workOrderRepository.save(workOrder);
            return Result.success(workOrder);

        } catch (IllegalArgumentException e) {
            return Result.failure(new WorkOrderCommandFailure.NotFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(new WorkOrderCommandFailure.InvalidState(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<WorkOrder, WorkOrderCommandFailure> handle(AddProductToTaskCommand command) {
        try {
            WorkOrder workOrder = findWorkOrderOrThrow(command.workOrderId());
            // Agrega el producto a la tarea y dispara el evento de stock temporal (Reserva)
            workOrder.addProductToTask(command.taskId(), command.productId(), command.quantity(), command.unitPrice());
            workOrderRepository.save(workOrder);
            return Result.success(workOrder);

        } catch (IllegalArgumentException e) {
            return Result.failure(new WorkOrderCommandFailure.NotFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(new WorkOrderCommandFailure.InvalidState(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<WorkOrder, WorkOrderCommandFailure> handle(RemoveProductFromTaskCommand command) {
        try {
            WorkOrder workOrder = findWorkOrderOrThrow(command.workOrderId());
            // Remueve el producto y dispara la liberación del stock temporal
            workOrder.removeProductFromTask(command.taskId(), command.productId());
            workOrderRepository.save(workOrder);
            return Result.success(workOrder);

        } catch (IllegalArgumentException e) {
            return Result.failure(new WorkOrderCommandFailure.NotFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(new WorkOrderCommandFailure.InvalidState(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<WorkOrder, WorkOrderCommandFailure> handle(RemoveTaskFromWorkOrderCommand command) {
        try {
            WorkOrder workOrder = findWorkOrderOrThrow(command.workOrderId());
            // Remueve la tarea y libera los stock temporales asociados
            workOrder.removeTask(command.taskId());
            workOrderRepository.save(workOrder);
            return Result.success(workOrder);

        } catch (IllegalArgumentException e) {
            return Result.failure(new WorkOrderCommandFailure.NotFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(new WorkOrderCommandFailure.InvalidState(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<WorkOrder, WorkOrderCommandFailure> handle(StartTaskCommand command) {
        try {
            WorkOrder workOrder = findWorkOrderOrThrow(command.workOrderId());
            // Pasa la tarea a DOING y estampa fecha de inicio
            workOrder.startTask(command.taskId());
            workOrderRepository.save(workOrder);
            return Result.success(workOrder);

        } catch (IllegalArgumentException e) {
            return Result.failure(new WorkOrderCommandFailure.NotFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(new WorkOrderCommandFailure.InvalidState(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<WorkOrder, WorkOrderCommandFailure> handle(CompleteTaskCommand command) {
        try {
            WorkOrder workOrder = findWorkOrderOrThrow(command.workOrderId());
            // Pasa la tarea a COMPLETED y estampa fecha de fin
            workOrder.completeTask(command.taskId());
            workOrderRepository.save(workOrder);
            return Result.success(workOrder);

        } catch (IllegalArgumentException e) {
            return Result.failure(new WorkOrderCommandFailure.NotFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(new WorkOrderCommandFailure.InvalidState(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<WorkOrder, WorkOrderCommandFailure> handle(ReopenTaskCommand command) {
        try {
            WorkOrder workOrder = findWorkOrderOrThrow(command.workOrderId());
            // Reabre la tarea de vuelta a DOING (habilitando edición en el frontend)
            workOrder.reopenTask(command.taskId());
            workOrderRepository.save(workOrder);
            return Result.success(workOrder);

        } catch (IllegalArgumentException e) {
            return Result.failure(new WorkOrderCommandFailure.NotFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(new WorkOrderCommandFailure.InvalidState(e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<WorkOrder, WorkOrderCommandFailure> handle(MarkWorkOrderAsPaidCommand command) {
        try {
            WorkOrder workOrder = findWorkOrderOrThrow(command.workOrderId());
            // Pasa la orden a PAID y dispara el descuento físico definitivo del inventario
            workOrder.markAsPaid();
            workOrderRepository.save(workOrder);
            return Result.success(workOrder);

        } catch (IllegalArgumentException e) {
            return Result.failure(new WorkOrderCommandFailure.NotFound(e.getMessage()));
        } catch (IllegalStateException e) {
            return Result.failure(new WorkOrderCommandFailure.InvalidState(e.getMessage()));
        }
    }

    /**
     * Helper para buscar la orden de trabajo o lanzar excepción controlada.
     */
    private WorkOrder findWorkOrderOrThrow(java.util.UUID workOrderId) {
        return workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new IllegalArgumentException("operations.error.workOrder.notFound"));
    }
}