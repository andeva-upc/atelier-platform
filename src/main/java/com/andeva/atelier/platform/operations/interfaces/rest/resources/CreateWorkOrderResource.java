package com.andeva.atelier.platform.operations.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

/**
 * REST resource for creating a new Work Order.
 */
public record CreateWorkOrderResource(
        @NotNull(message = "operations.error.resource.appointmentId.required")
        UUID appointmentId,

        @NotNull(message = "operations.error.resource.branchId.required")
        UUID branchId,

        @NotNull(message = "operations.error.resource.vehicleId.required")
        UUID vehicleId,

        @NotNull(message = "operations.error.resource.customerId.required")
        UUID customerId,

        @NotNull(message = "operations.error.resource.internalNumber.required")
        Integer internalNumber,

        @NotNull(message = "operations.error.resource.diagnosticSummary.required")
        @Size(min = 5, max = 2000, message = "operations.error.resource.diagnosticSummary.size")
        String diagnosticSummary,

        @NotNull(message = "operations.error.resource.mileageIn.required")
        Integer mileageIn
) {}