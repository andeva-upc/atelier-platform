package com.andeva.atelier.platform.operations.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * REST resource representing a Work Order Task response payload.
 */
public record WorkOrderTaskResource(
        UUID id,
        UUID serviceId,
        UUID branchId,
        UUID assignedMechanicId,
        String status,
        String description,
        BigDecimal price, // Precio total acumulado de la tarea (Mano de obra + Insumos)
        Instant startedAt,
        Instant completedAt,
        List<WorkOrderTaskProductResource> products, // Repuestos anidados en esta tarea
        Instant createdAt
) {}