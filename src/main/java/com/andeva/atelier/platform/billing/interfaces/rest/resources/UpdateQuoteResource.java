package com.andeva.atelier.platform.billing.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Resource representing the payload to update a quote's discount.
 * 
 * @param discountPercentage The new discount percentage to apply (must be between 0 and 100).
 */
public record UpdateQuoteResource(
        @NotNull(message = "Discount percentage is required")
        @Min(value = 0, message = "Discount cannot be less than 0")
        @Max(value = 100, message = "Discount cannot be greater than 100") Double discountPercentage
) {}
