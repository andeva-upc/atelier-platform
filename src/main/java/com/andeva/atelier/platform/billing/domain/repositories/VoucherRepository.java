package com.andeva.atelier.platform.billing.domain.repositories;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Voucher;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;

public interface VoucherRepository {
    Voucher save(Voucher voucher);
    Optional<Voucher> findById(UUID id);
    List<Voucher> findByBranchId(BranchId branchId);
}
