package com.andeva.atelier.platform.billing.domain.model.entities;

import com.andeva.atelier.platform.billing.domain.model.valueobjects.PaymentMethod;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Payment {
    private UUID id;
    private Money amount;
    private PaymentMethod method;
    private UUID branchId;

    public Payment() {
        // Required for persistence
    }

    public Payment(Money amount, PaymentMethod method, UUID branchId) {
        if (amount == null || amount.amount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        if (method == null) {
            throw new IllegalArgumentException("Payment method is required");
        }
        if (branchId == null) {
            throw new IllegalArgumentException("Branch ID is required for a payment");
        }
        
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.method = method;
        this.branchId = branchId;
    }

    // For persistence rebuilding
    public Payment(UUID id, Money amount, PaymentMethod method, UUID branchId) {
        this.id = id;
        this.amount = amount;
        this.method = method;
        this.branchId = branchId;
    }
}
