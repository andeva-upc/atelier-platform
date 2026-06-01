# Operations Bounded Context

Este documento describe en detalle todas las funcionalidades implementadas en el **Bounded Context de Operations** de la plataforma Atelier. Este módulo se encarga de gestionar el flujo completo de las órdenes de trabajo (Work Orders), las tareas asignadas a los mecánicos y el uso de productos/repuestos.

## 1. Gestión de Órdenes de Trabajo (Work Orders)

La Orden de Trabajo (Work Order) es el Aggregate Root principal de este bounded context. Representa el documento central sobre el que se registra el estado del vehículo y todas las reparaciones o servicios a realizar.

*   **Crear Orden de Trabajo (`CreateWorkOrderCommand`)**:
    *   Permite generar una nueva Work Order asociada a una cita (`Appointment`), sucursal (`Branch`), vehículo (`Vehicle`) y cliente (`Customer`).
    *   Registra el diagnóstico inicial (`DiagnosticSummary`) y el kilometraje de entrada (`MileageIn`).
    *   Asigna automáticamente un número interno consecutivo (`InternalNumber`) único por sucursal.
    *   La orden inicia en estado `PENDING` (Pendiente).
*   **Actualizar Detalles de la Orden (`UpdateWorkOrderDetailsCommand`)**:
    *   Permite modificar el resumen de diagnóstico y el kilometraje, siempre y cuando la orden no esté en estado `COMPLETED` o `PAID`.
*   **Eliminar Orden de Trabajo (`DeleteWorkOrderCommand`)**:
    *   Realiza un borrado lógico (*soft delete*) de la orden cambiando el campo `deleted_at`.
    *   Libera automáticamente cualquier producto o repuesto que hubiera estado reservado en las tareas de esta orden.
*   **Marcar Orden como Pagada (`MarkWorkOrderAsPaidCommand`)**:
    *   Cambia el estado de la Work Order a `PAID`.
    *   Desencadena eventos de dominio para notificar a otros bounded contexts (como el módulo de inventario) sobre el despacho definitivo de los productos utilizados.
*   **Completar Orden de Trabajo**:
    *   El sistema permite completar automáticamente la orden cuando todas las tareas en su interior pasan a estado `COMPLETED`.

## 2. Gestión de Tareas (Tasks)

Las tareas representan el trabajo específico que realizará un mecánico sobre el vehículo.

*   **Agregar Tarea (`AddTaskToWorkOrderCommand`)**:
    *   Permite añadir un nuevo servicio o tarea a la orden de trabajo.
    *   Requiere especificar el servicio, el mecánico asignado, una descripción y el precio de la mano de obra.
    *   La tarea inicia en estado `PENDING`.
*   **Actualizar Detalles de la Tarea (`UpdateWorkOrderTaskDetailsCommand`)**:
    *   Permite modificar el servicio, el mecánico, la descripción y el costo de mano de obra de una tarea existente.
    *   Recalcula automáticamente el costo total de la orden.
*   **Eliminar Tarea (`RemoveTaskFromWorkOrderCommand`)**:
    *   Borra una tarea específica y libera cualquier reserva de stock (productos) que estuviera asociada a la misma.
    *   No se puede eliminar una tarea si ya está marcada como completada.
*   **Iniciar Tarea (`StartTaskCommand`)**:
    *   Cambia el estado de la tarea de `PENDING` a `IN_PROGRESS` y registra la fecha de inicio.
    *   Si la Work Order estaba en `PENDING`, automáticamente cambia su estado a `IN_PROGRESS`.
*   **Completar Tarea (`CompleteTaskCommand`)**:
    *   Cambia el estado de la tarea a `COMPLETED` y registra la fecha de finalización.
    *   Si es la última tarea pendiente de la orden, la Work Order completa pasará a estado `COMPLETED`.
*   **Reabrir Tarea (`ReopenTaskCommand`)**:
    *   Devuelve una tarea completada a estado `IN_PROGRESS`, eliminando su fecha de finalización.
    *   Si la Work Order estaba como `COMPLETED`, retrocede automáticamente su estado a `IN_PROGRESS`.

## 3. Gestión de Productos/Repuestos en Tareas

Los mecánicos pueden asociar repuestos físicos a las tareas, lo cual tiene un impacto directo en el inventario.

*   **Agregar Producto a una Tarea (`AddProductToTaskCommand`)**:
    *   Añade un repuesto a la tarea indicando la cantidad y el precio unitario.
    *   El costo del producto se suma al costo total de la orden.
    *   Dispara un evento de dominio (`ProductReservedEvent`) para reservar el inventario en el almacén de la sucursal correspondiente.
*   **Actualizar Cantidad del Producto (`UpdateProductQuantityInTaskCommand`)**:
    *   Modifica la cantidad de un repuesto en la tarea.
    *   Recalcula el total de la orden de trabajo.
    *   Calcula la diferencia (delta) y emite un evento de reserva (`ProductReservedEvent`) si la cantidad aumentó, o de cancelación de reserva (`ProductReservationCanceledEvent`) si la cantidad disminuyó.
*   **Eliminar Producto de la Tarea (`RemoveProductFromTaskCommand`)**:
    *   Quita el repuesto de la tarea.
    *   Emite un evento (`ProductReservationCanceledEvent`) para devolver las cantidades reservadas de nuevo al stock disponible.

## 4. Consultas (Queries)

El módulo provee capacidades de lectura optimizadas para consultar el estado de las operaciones:

*   **Obtener Orden por ID (`GetWorkOrderByIdQuery`)**:
    *   Recupera toda la información detallada de una orden de trabajo, incluyendo sus tareas, productos y el prefijo formateado de la sucursal.
*   **Listar Órdenes por Sucursal (`GetWorkOrdersByBranchIdQuery`)**:
    *   Endpoint multi-tenant diseñado para retornar todas las órdenes de trabajo activas de una sucursal (`Branch`) específica.
*   **Historial de Servicio por Vehículo (`GetWorkOrdersByVehicleIdQuery`)**:
    *   Retorna el historial completo de todas las órdenes de trabajo realizadas a un vehículo específico, independientemente de la sucursal en donde se atendió.

## 5. Diseño REST Pragmático

El API REST para este bounded context sigue buenas prácticas, dividiendo adecuadamente las ediciones CRUD tradicionales de las **intenciones de negocio**:
*   Mutaciones directas utilizan `PUT` (ej. actualizar detalles).
*   Mutaciones de recursos hijos utilizan `POST` (ej. crear tareas o agregar productos).
*   Las acciones de negocio o cambios de estado complejos (`start`, `complete`, `reopen`) se exponen como sub-recursos con el verbo `POST` para indicar con claridad su intención operativa (Ej. `POST /api/v1/work-orders/{id}/tasks/{taskId}/start`).
