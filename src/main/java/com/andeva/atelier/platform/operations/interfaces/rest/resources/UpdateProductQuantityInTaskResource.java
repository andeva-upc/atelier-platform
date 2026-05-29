package com.andeva.atelier.platform.operations.interfaces.rest.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateProductQuantityInTaskResource(
        @NotNull(message = "operations.error.resource.quantity.required")
        @Min(value = 1, message = "operations.error.resource.quantity.invalid")
        Integer quantity
) {}
