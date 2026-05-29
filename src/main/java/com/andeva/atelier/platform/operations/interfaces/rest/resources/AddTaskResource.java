package com.andeva.atelier.platform.operations.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

public record AddTaskResource(
        @NotNull(message = "operations.error.resource.serviceId.required")
        UUID serviceId,

        @NotNull(message = "operations.error.resource.assignedMechanicId.required")
        UUID assignedMechanicId,

        @NotNull(message = "operations.error.resource.description.required")
        @Size(min = 5, max = 1000, message = "operations.error.resource.description.size")
        String description,

        @NotNull(message = "operations.error.resource.laborPrice.required")
        BigDecimal laborPrice
) {}