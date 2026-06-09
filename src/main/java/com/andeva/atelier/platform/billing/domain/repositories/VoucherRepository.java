package com.andeva.atelier.platform.billing.domain.repositories;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Voucher;

import java.util.Optional;
import java.util.UUID;

public interface VoucherRepository {
    Voucher save(Voucher voucher);
    Optional<Voucher> findById(UUID id);
}
