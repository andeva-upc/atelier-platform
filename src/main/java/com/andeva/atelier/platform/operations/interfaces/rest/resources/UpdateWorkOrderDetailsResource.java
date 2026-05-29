package com.andeva.atelier.platform.operations.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateWorkOrderDetailsResource(
        @NotNull(message = "operations.error.resource.diagnosticSummary.required")
        @Size(min = 5, max = 2000, message = "operations.error.resource.diagnosticSummary.size")
        String diagnosticSummary,
        @NotNull(message = "operations.error.resource.mileageIn.required")
        Integer mileageIn
) {}
