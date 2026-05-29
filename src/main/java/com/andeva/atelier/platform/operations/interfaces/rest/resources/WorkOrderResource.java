package com.andeva.atelier.platform.operations.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * REST resource representing a Work Order response payload.
 */
public record WorkOrderResource(
        UUID id,
        UUID appointmentId,
        UUID branchId,
        UUID vehicleId,
        UUID customerId,
        Integer internalNumber,
        String status,
        String diagnosticSummary,
        Integer mileageIn,
        BigDecimal totalAmount,
        List<WorkOrderTaskResource> tasks, // Lista de tareas internas formateadas
        Instant createdAt,
        Instant updatedAt
) {}