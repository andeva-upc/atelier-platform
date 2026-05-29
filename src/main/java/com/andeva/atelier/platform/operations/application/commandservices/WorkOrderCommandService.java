package com.andeva.atelier.platform.operations.application.commandservices;

import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrder;
import com.andeva.atelier.platform.operations.domain.model.commands.*;
import com.andeva.atelier.platform.shared.application.result.Result;

/**
 * Application service contract for executing Work Order command operations (use cases).
 * Returns a Result mapping either the successfully updated aggregate or a specific business failure.
 */
public interface WorkOrderCommandService {

    /**
     * Crea e inicializa una nueva Orden de Trabajo.
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(CreateWorkOrderCommand command);

    /**
     * Agrega una nueva tarea de servicio a la orden de trabajo.
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(AddTaskToWorkOrderCommand command);

    /**
     * Agrega un repuesto/insumo a una tarea de la orden y ejecuta la reserva temporal.
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(AddProductToTaskCommand command);

    /**
     * Remueve un repuesto de una tarea y libera su reserva del stock.
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(RemoveProductFromTaskCommand command);

    /**
     * Remueve una tarea completa de la orden y libera todos sus repuestos asociados del stock.
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(RemoveTaskFromWorkOrderCommand command);

    /**
     * Inicia una tarea (pasa a DOING y estampa fecha de inicio).
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(StartTaskCommand command);

    /**
     * Completa una tarea técnica (pasa a COMPLETED).
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(CompleteTaskCommand command);

    /**
     * Reabre una tarea técnica mediante tu botón especial (pasa a DOING y libera stock temporal).
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(ReopenTaskCommand command);

    /**
     * Paga la orden de trabajo (pasa a PAID y ejecuta el descuento físico definitivo del inventario).
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(MarkWorkOrderAsPaidCommand command);
}