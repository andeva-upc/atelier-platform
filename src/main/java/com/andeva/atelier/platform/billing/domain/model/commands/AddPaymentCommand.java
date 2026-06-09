package com.andeva.atelier.platform.billing.domain.model.commands;

import com.andeva.atelier.platform.billing.domain.model.valueobjects.PaymentMethod;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;

import java.util.UUID;

public record AddPaymentCommand(UUID voucherId, Money amount, PaymentMethod method) {
    public AddPaymentCommand {
        if (voucherId == null) {
            throw new IllegalArgumentException("Voucher ID is required");
        }
        if (amount == null || amount.amount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        if (method == null) {
            throw new IllegalArgumentException("Payment method is required");
        }
    }
}
