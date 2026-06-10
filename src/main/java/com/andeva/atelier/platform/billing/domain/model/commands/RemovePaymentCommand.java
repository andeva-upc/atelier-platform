package com.andeva.atelier.platform.billing.domain.model.commands;

import java.util.UUID;

public record RemovePaymentCommand(UUID voucherId, UUID paymentId) {
    public RemovePaymentCommand {
        if (voucherId == null) {
            throw new IllegalArgumentException("voucherId cannot be null");
        }
        if (paymentId == null) {
            throw new IllegalArgumentException("paymentId cannot be null");
        }
    }
}
