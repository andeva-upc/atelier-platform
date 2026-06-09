package com.andeva.atelier.platform.billing.domain.model.aggregates;

import com.andeva.atelier.platform.billing.domain.model.valueobjects.VoucherStatus;
import com.andeva.atelier.platform.billing.domain.model.valueobjects.VoucherType;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import lombok.Getter;

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
}
