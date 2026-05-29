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
     * Reabre una tarea técnica mediante el botón especial en el frontend (retorna la tarea a DOING 
     * para permitir editar o agregar más productos, manteniendo la reserva de stock temporal intacta).
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(ReopenTaskCommand command);

    /**
     * Paga la orden de trabajo (pasa a PAID y ejecuta el descuento físico definitivo del inventario).
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(MarkWorkOrderAsPaidCommand command);

    /**
     * Actualiza los detalles de la orden de trabajo (diagnóstico y kilometraje).
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(UpdateWorkOrderDetailsCommand command);

    /**
     * Actualiza los detalles de una tarea mecánica.
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(UpdateWorkOrderTaskDetailsCommand command);

    /**
     * Modifica la cantidad reservada de un producto en una tarea.
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(UpdateProductQuantityInTaskCommand command);

    /**
     * Elimina lógicamente una orden de trabajo completa (Soft Delete) y libera todas sus reservas de stock.
     */
    Result<WorkOrder, WorkOrderCommandFailure> handle(DeleteWorkOrderCommand command);
}