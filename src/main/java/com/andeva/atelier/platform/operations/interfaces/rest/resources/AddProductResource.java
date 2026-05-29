package com.andeva.atelier.platform.operations.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record AddProductResource(
        @NotNull(message = "operations.error.resource.productId.required")
        UUID productId,

        @NotNull(message = "operations.error.resource.quantity.required")
        Integer quantity,

        @NotNull(message = "operations.error.resource.unitPrice.required")
        BigDecimal unitPrice
) {}