package com.andeva.atelier.platform.billing.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record GenerateVoucherResource(
        @NotNull
        UUID quoteId,
        @NotBlank
        String type,
        @NotBlank
        String customerDocumentType,
        @NotBlank
        String customerDocumentNumber,
        @NotBlank
        String customerName
) {
}
