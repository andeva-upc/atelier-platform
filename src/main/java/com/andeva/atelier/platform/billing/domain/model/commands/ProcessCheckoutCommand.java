package com.andeva.atelier.platform.billing.domain.model.commands;

import com.andeva.atelier.platform.billing.domain.model.valueobjects.PaymentMethod;
import com.andeva.atelier.platform.billing.domain.model.valueobjects.VoucherType;

import java.util.UUID;

public record ProcessCheckoutCommand(
        UUID quoteId,
        VoucherType type,
        String customerDocumentType,
        String customerDocumentNumber,
        String customerName,
        PaymentMethod method
) {
    public ProcessCheckoutCommand {
        if (quoteId == null) {
            throw new IllegalArgumentException("quoteId cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (customerDocumentType == null || customerDocumentType.isBlank()) {
            throw new IllegalArgumentException("customerDocumentType cannot be null or empty");
        }
        if (customerDocumentNumber == null || customerDocumentNumber.isBlank()) {
            throw new IllegalArgumentException("customerDocumentNumber cannot be null or empty");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("customerName cannot be null or empty");
        }
        if (method == null) {
            throw new IllegalArgumentException("method cannot be null");
        }
    }
}
