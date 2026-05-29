package com.andeva.atelier.platform.operations.application.queryservices;

import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrder;
import com.andeva.atelier.platform.operations.domain.model.queries.*;
import java.util.List;
import java.util.Optional;

/**
 * Application service contract providing read access to Work Orders.
 * Exposes query operations used by the interface layer to retrieve
 * data without exposing persistence details.
 */
public interface WorkOrderQueryService {

    /**
     * Recupera una Orden de Trabajo por su identificador único (ID).
     */
    Optional<WorkOrder> handle(GetWorkOrderByIdQuery query);

    /**
     * Recupera todas las órdenes de trabajo pertenecientes a una sucursal (Multi-tenant).
     */
    List<WorkOrder> handle(GetWorkOrdersByBranchIdQuery query);

    /**
     * Recupera el historial clínico de reparaciones de un vehículo específico.
     */
    List<WorkOrder> handle(GetWorkOrdersByVehicleIdQuery query);
}
