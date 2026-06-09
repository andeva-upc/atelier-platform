package com.andeva.atelier.platform.billing.application.queryservices;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Voucher;
import com.andeva.atelier.platform.billing.domain.model.queries.GetVoucherByIdQuery;

import java.util.Optional;

/**
 * Service interface for querying Vouchers.
 */
public interface VoucherQueryService {
    /**
     * Retrieves a Voucher by its unique identifier.
     *
     * @param query the query containing the voucher ID
     * @return an Optional containing the Voucher if found, or empty otherwise
     */
    Optional<Voucher> handle(GetVoucherByIdQuery query);
}
