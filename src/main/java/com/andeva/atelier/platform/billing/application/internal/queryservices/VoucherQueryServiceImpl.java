package com.andeva.atelier.platform.billing.application.internal.queryservices;

import com.andeva.atelier.platform.billing.application.queryservices.VoucherQueryService;
import com.andeva.atelier.platform.billing.domain.model.aggregates.Voucher;
import com.andeva.atelier.platform.billing.domain.model.queries.GetVoucherByIdQuery;
import com.andeva.atelier.platform.billing.domain.repositories.VoucherRepository;
import com.andeva.atelier.platform.billing.domain.model.queries.GetVouchersByBranchIdQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Service
public class VoucherQueryServiceImpl implements VoucherQueryService {

    private final VoucherRepository voucherRepository;

    public VoucherQueryServiceImpl(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Voucher> handle(GetVoucherByIdQuery query) {
        return voucherRepository.findById(query.voucherId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Voucher> handle(GetVouchersByBranchIdQuery query) {
        return voucherRepository.findByBranchId(query.branchId());
    }
}
