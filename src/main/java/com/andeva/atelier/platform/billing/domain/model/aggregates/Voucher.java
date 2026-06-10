package com.andeva.atelier.platform.billing.domain.model.aggregates;

import com.andeva.atelier.platform.billing.domain.model.valueobjects.VoucherStatus;
import com.andeva.atelier.platform.billing.domain.model.valueobjects.VoucherType;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import lombok.Getter;

import com.andeva.atelier.platform.billing.domain.model.entities.Payment;
import com.andeva.atelier.platform.billing.domain.model.valueobjects.PaymentMethod;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.andeva.atelier.platform.billing.domain.model.events.VoucherPaidEvent;
import java.util.UUID;
/**
 * Aggregate root representing a Voucher (Invoice or Receipt) in the billing context.
 * A Voucher is generated from an APPROVED Quote and represents the financial obligation
 * of the customer to pay for the services rendered.
 */
@Getter
public class Voucher extends AbstractDomainAggregateRoot<Voucher> {

    private UUID id;
    private UUID quoteId;
    private VoucherType type;
    private String customerDocumentType;
    private String customerDocumentNumber;
    private String customerName;
    private Money totalAmount;
    private VoucherStatus status;
    private UUID externalInvoiceId; // ID returned by the Facthub service
    private List<Payment> payments = new ArrayList<>();
    public Voucher() {
        // Required by persistence assembler
    }

    public Voucher(UUID quoteId, VoucherType type, String customerDocumentType, 
                   String customerDocumentNumber, String customerName, 
                   Money totalAmount, UUID externalInvoiceId) {
        if (totalAmount == null || totalAmount.amount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Voucher total amount must be greater than zero");
        }
        
        this.id = UUID.randomUUID();
        this.quoteId = quoteId;
        this.type = type;
        this.customerDocumentType = customerDocumentType;
        this.customerDocumentNumber = customerDocumentNumber;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = VoucherStatus.PENDING;
        this.externalInvoiceId = externalInvoiceId;
    }

    // For persistence rebuilding
    public Voucher(UUID id, UUID quoteId, VoucherType type, String customerDocumentType, 
                   String customerDocumentNumber, String customerName, 
                   Money totalAmount, VoucherStatus status, UUID externalInvoiceId) {
        this.id = id;
        this.quoteId = quoteId;
        this.type = type;
        this.customerDocumentType = customerDocumentType;
        this.customerDocumentNumber = customerDocumentNumber;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.externalInvoiceId = externalInvoiceId;
    }

    // For persistence rebuilding with payments
    public Voucher(UUID id, UUID quoteId, VoucherType type, String customerDocumentType, 
                   String customerDocumentNumber, String customerName, 
                   Money totalAmount, VoucherStatus status, UUID externalInvoiceId, List<Payment> payments) {
        this.id = id;
        this.quoteId = quoteId;
        this.type = type;
        this.customerDocumentType = customerDocumentType;
        this.customerDocumentNumber = customerDocumentNumber;
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
        this.externalInvoiceId = externalInvoiceId;
        if (payments != null) {
            this.payments = payments;
        }
    }

    public List<Payment> getPayments() {
        return Collections.unmodifiableList(payments);
    }

    public BigDecimal getTotalPaidAmount() {
        return payments.stream()
                .map(p -> p.getAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addPayment(Money amount, PaymentMethod method, UUID branchId) {
        if (this.status == VoucherStatus.CANCELED) {
            throw new IllegalStateException("Cannot add payment to a canceled voucher");
        }
        if (this.status == VoucherStatus.PAID) {
            throw new IllegalStateException("Voucher is already paid in full");
        }

        BigDecimal currentTotalPaid = getTotalPaidAmount();
        BigDecimal newTotalPaid = currentTotalPaid.add(amount.amount());

        if (newTotalPaid.compareTo(this.totalAmount.amount()) > 0) {
            throw new IllegalStateException("Payment exceeds the total debt of the voucher");
        }

        this.payments.add(new Payment(amount, method, branchId));

        if (newTotalPaid.compareTo(this.totalAmount.amount()) == 0) {
            this.status = VoucherStatus.PAID;
            this.registerDomainEvent(new VoucherPaidEvent(this, this.id, this.quoteId));
        } else {
            this.status = VoucherStatus.PARTIALLY_PAID;
        }
    }

    public void removePayment(UUID paymentId) {
        if (this.status == VoucherStatus.CANCELED) {
            throw new IllegalStateException("Cannot remove payment from a canceled voucher");
        }

        Payment paymentToRemove = this.payments.stream()
                .filter(p -> p.getId().equals(paymentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        this.payments.remove(paymentToRemove);

        BigDecimal currentTotalPaid = getTotalPaidAmount();

        if (currentTotalPaid.compareTo(BigDecimal.ZERO) == 0) {
            this.status = VoucherStatus.PENDING;
        } else if (currentTotalPaid.compareTo(this.totalAmount.amount()) < 0) {
            this.status = VoucherStatus.PARTIALLY_PAID;
        } else {
            this.status = VoucherStatus.PAID;
        }
    }
}
