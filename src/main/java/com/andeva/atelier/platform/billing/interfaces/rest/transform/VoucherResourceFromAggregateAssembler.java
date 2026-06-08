package com.andeva.atelier.platform.billing.interfaces.rest.transform;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Voucher;
import com.andeva.atelier.platform.billing.interfaces.rest.resources.VoucherResource;

public class VoucherResourceFromAggregateAssembler {

    public static VoucherResource toResourceFromAggregate(Voucher aggregate) {
        return new VoucherResource(
                aggregate.getId(),
                aggregate.getQuoteId(),
                aggregate.getType().name(),
                aggregate.getCustomerDocumentType(),
                aggregate.getCustomerDocumentNumber(),
                aggregate.getCustomerName(),
                aggregate.getTotalAmount().amount(),
                aggregate.getStatus().name(),
                aggregate.getExternalInvoiceId()
        );
    }
}
