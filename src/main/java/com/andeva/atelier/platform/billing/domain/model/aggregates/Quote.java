package com.andeva.atelier.platform.billing.domain.model.aggregates;

import com.andeva.atelier.platform.billing.domain.model.commands.CreateQuoteCommand;
import com.andeva.atelier.platform.billing.domain.model.valueobjects.QuoteStatus;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Quote extends AbstractDomainAggregateRoot<Quote> {

    private UUID id;
    private UUID workOrderId;
    private BranchId branchId;
    private Money subtotalAmount;
    private Double discountPercentage;
    private Money totalAmount;
    private QuoteStatus status;

    public Quote() {
        // Required by persistence assembler
    }

    public Quote(CreateQuoteCommand command, Money subtotalAmount) {
        this.id = UUID.randomUUID();
        this.workOrderId = command.workOrderId();
        this.branchId = command.branchId();
        this.subtotalAmount = subtotalAmount;
        this.discountPercentage = command.discountPercentage();
        this.status = QuoteStatus.DRAFT;
        
        calculateTotal();
    }

    private void calculateTotal() {
        if (this.subtotalAmount == null) {
            this.totalAmount = Money.ZERO;
            return;
        }
        double discountFactor = 1.0 - (this.discountPercentage / 100.0);
        this.totalAmount = this.subtotalAmount.multiply(java.math.BigDecimal.valueOf(discountFactor));
    }

    public void approve() {
        if (this.status != QuoteStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT quotes can be approved.");
        }
        this.status = QuoteStatus.APPROVED;
    }

    public void cancel() {
        if (this.status == QuoteStatus.APPROVED) {
            throw new IllegalStateException("APPROVED quotes cannot be canceled directly. Revert them first.");
        }
        this.status = QuoteStatus.CANCELED;
    }
    
    public void updateDiscount(Double newDiscount) {
        if (this.status != QuoteStatus.DRAFT) {
            throw new IllegalStateException("Only DRAFT quotes can be updated.");
        }
        if (newDiscount < 0 || newDiscount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }
        this.discountPercentage = newDiscount;
        calculateTotal();
    }
    
    // For persistence rebuilding
    public Quote(UUID id, UUID workOrderId, BranchId branchId, Money subtotalAmount, Double discountPercentage, Money totalAmount, QuoteStatus status) {
        this.id = id;
        this.workOrderId = workOrderId;
        this.branchId = branchId;
        this.subtotalAmount = subtotalAmount;
        this.discountPercentage = discountPercentage;
        this.totalAmount = totalAmount;
        this.status = status;
    }
}
