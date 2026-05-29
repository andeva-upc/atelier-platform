package com.andeva.atelier.platform.operations.application.commandservices;

public sealed interface WorkOrderCommandFailure permits
        WorkOrderCommandFailure.NotFound,
        WorkOrderCommandFailure.InvalidState,
        WorkOrderCommandFailure.Duplicate {
    /**
     * Ocurre cuando un recurso (WorkOrder, Task o Producto) no existe en la base de datos.
     */
    record NotFound(String message) implements WorkOrderCommandFailure {}
    /**
     * Ocurre cuando se violan reglas de transición de estado o invariants del negocio
     * (ej. intentar modificar una orden ya pagada).
     */
    record InvalidState(String message) implements WorkOrderCommandFailure {}
    /**
     * Ocurre cuando se intenta crear un recurso duplicado (ej. una orden para una cita ya procesada).
     */
    record Duplicate(String message) implements WorkOrderCommandFailure {}
}
