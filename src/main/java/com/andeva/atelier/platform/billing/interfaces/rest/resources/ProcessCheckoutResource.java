package com.andeva.atelier.platform.billing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ProcessCheckoutResource(
        @NotNull(message = "Quote ID is required") UUID quoteId,
        @NotBlank(message = "Voucher type is required") String type,
        @NotBlank(message = "Customer document type is required") String customerDocumentType,
        @NotBlank(message = "Customer document number is required") String customerDocumentNumber,
        @NotBlank(message = "Customer name is required") String customerName,
        @NotBlank(message = "Payment method is required") String method
) {
}
