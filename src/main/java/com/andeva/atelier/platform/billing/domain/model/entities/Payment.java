package com.andeva.atelier.platform.billing.domain.model.entities;

import com.andeva.atelier.platform.billing.domain.model.valueobjects.PaymentMethod;
import com.andeva.atelier.platform.shared.domain.model.entities.AuditableModel;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Payment extends AuditableModel {
    private UUID id;
    private Money amount;
    private PaymentMethod method;

    public Payment() {
        // Required for persistence
    }

    public Payment(Money amount, PaymentMethod method) {
        if (amount == null || amount.amount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        if (method == null) {
            throw new IllegalArgumentException("Payment method is required");
        }
        
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.method = method;
    }

    // For persistence rebuilding
    public Payment(UUID id, Money amount, PaymentMethod method) {
        this.id = id;
        this.amount = amount;
        this.method = method;
    }
}
