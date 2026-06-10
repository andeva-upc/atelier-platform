package com.andeva.atelier.platform.billing.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * DTO representing the payload required to create a new Quote via the REST API.
 */
public record CreateQuoteResource(
        @NotNull(message = "Work Order ID is required") UUID workOrderId,
        @NotNull(message = "Branch ID is required") UUID branchId,
        @NotNull(message = "Discount percentage is required") 
        @Min(value = 0, message = "Discount cannot be less than 0")
        @Max(value = 100, message = "Discount cannot be greater than 100") Double discountPercentage
) {}
