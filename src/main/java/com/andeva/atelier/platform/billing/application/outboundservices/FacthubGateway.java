package com.andeva.atelier.platform.billing.application.outboundservices;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Quote;
import com.andeva.atelier.platform.billing.domain.model.valueobjects.VoucherType;

import java.util.Optional;
import java.util.UUID;

public interface FacthubGateway {
    
    /**
     * Issues an electronic voucher via the Facthub Service API.
     * 
     * @param issuerRuc The RUC of the workshop issuing the voucher.
     * @param documentType The type of voucher (RECEIPT or INVOICE).
     * @param customerDocumentType The document type of the customer (e.g., DNI, RUC).
     * @param customerDocumentNumber The document number of the customer.
     * @param customerName The name of the customer.
     * @param quote The approved quote containing the amounts and items.
     * @return The UUID assigned to the invoice by Facthub, or Optional.empty() if it failed.
     */
    Optional<UUID> issueVoucher(
            String issuerRuc, 
            VoucherType documentType, 
            String customerDocumentType, 
            String customerDocumentNumber, 
            String customerName, 
            Quote quote
    );
}
