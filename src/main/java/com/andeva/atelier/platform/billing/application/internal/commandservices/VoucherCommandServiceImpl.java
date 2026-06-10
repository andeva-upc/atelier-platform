package com.andeva.atelier.platform.billing.application.internal.commandservices;

import com.andeva.atelier.platform.billing.domain.model.valueobjects.VoucherCommandFailure;
import com.andeva.atelier.platform.billing.application.commandservices.VoucherCommandService;
import com.andeva.atelier.platform.billing.application.outboundservices.FacthubGateway;
import com.andeva.atelier.platform.billing.domain.model.aggregates.Voucher;
import com.andeva.atelier.platform.billing.domain.model.commands.AddPaymentCommand;
import com.andeva.atelier.platform.billing.domain.model.commands.GenerateVoucherCommand;
import com.andeva.atelier.platform.billing.domain.model.valueobjects.QuoteStatus;
import com.andeva.atelier.platform.billing.domain.repositories.QuoteRepository;
import com.andeva.atelier.platform.billing.domain.repositories.VoucherRepository;
import com.andeva.atelier.platform.core.application.queryservices.BranchQueryService;
import com.andeva.atelier.platform.core.application.queryservices.WorkshopQueryService;
import com.andeva.atelier.platform.core.domain.model.queries.GetBranchByIdQuery;
import com.andeva.atelier.platform.core.domain.model.queries.GetWorkshopByIdQuery;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoucherCommandServiceImpl implements VoucherCommandService {

    private final VoucherRepository voucherRepository;
    private final QuoteRepository quoteRepository;
    private final BranchQueryService branchQueryService;
    private final WorkshopQueryService workshopQueryService;
    private final FacthubGateway facthubGateway;

    public VoucherCommandServiceImpl(
            VoucherRepository voucherRepository,
            QuoteRepository quoteRepository,
            BranchQueryService branchQueryService,
            WorkshopQueryService workshopQueryService,
            FacthubGateway facthubGateway) {
        this.voucherRepository = voucherRepository;
        this.quoteRepository = quoteRepository;
        this.branchQueryService = branchQueryService;
        this.workshopQueryService = workshopQueryService;
        this.facthubGateway = facthubGateway;
    }

    @Override
    @Transactional
    public Result<Voucher, VoucherCommandFailure> handle(GenerateVoucherCommand command) {
        // 1. Get and validate Quote
        var quoteOpt = quoteRepository.findById(command.quoteId());
        if (quoteOpt.isEmpty()) {
            return Result.failure(VoucherCommandFailure.QUOTE_NOT_FOUND);
        }
        var quote = quoteOpt.get();
        if (quote.getStatus() != QuoteStatus.APPROVED) {
            return Result.failure(VoucherCommandFailure.QUOTE_NOT_APPROVED);
        }

        // 2. Query Core context for Issuer RUC (Tax ID)
        var coreBranchId = new BranchId(quote.getBranchId().value());
        var branchOpt = branchQueryService.handle(new GetBranchByIdQuery(coreBranchId));
        if (branchOpt.isEmpty()) {
            return Result.failure(VoucherCommandFailure.ISSUER_NOT_FOUND);
        }
        
        var workshopOpt = workshopQueryService.handle(new GetWorkshopByIdQuery(branchOpt.get().getWorkshopId()));
        if (workshopOpt.isEmpty()) {
            return Result.failure(VoucherCommandFailure.ISSUER_NOT_FOUND);
        }
        
        String issuerRuc = String.valueOf(workshopOpt.get().getTaxId());

        // 3. Issue Voucher via Facthub
        var externalInvoiceIdOpt = facthubGateway.issueVoucher(
                issuerRuc,
                command.type(),
                command.customerDocumentType(),
                command.customerDocumentNumber(),
                command.customerName(),
                quote
        );

        if (externalInvoiceIdOpt.isEmpty()) {
            return Result.failure(VoucherCommandFailure.FACTHUB_ISSUANCE_FAILED);
        }

        // 4. Create and save Voucher Aggregate
        try {
            var voucher = new Voucher(
                    command.quoteId(),
                    command.type(),
                    command.customerDocumentType(),
                    command.customerDocumentNumber(),
                    command.customerName(),
                    quote.getTotalAmount(),
                    externalInvoiceIdOpt.get()
            );

            var savedVoucher = voucherRepository.save(voucher);
            return Result.success(savedVoucher);
        } catch (IllegalArgumentException e) {
            return Result.failure(VoucherCommandFailure.INVALID_VOUCHER_DATA);
        }
    }

    @Override
    @Transactional
    public Result<Voucher, VoucherCommandFailure> handle(AddPaymentCommand command) {
        var voucherOpt = voucherRepository.findById(command.voucherId());
        if (voucherOpt.isEmpty()) {
            return Result.failure(VoucherCommandFailure.VOUCHER_NOT_FOUND);
        }

        var voucher = voucherOpt.get();
        var quoteOpt = quoteRepository.findById(voucher.getQuoteId());
        if (quoteOpt.isEmpty()) {
            return Result.failure(VoucherCommandFailure.QUOTE_NOT_FOUND);
        }
        var branchId = quoteOpt.get().getBranchId().value();

        try {
            voucher.addPayment(command.amount(), command.method(), branchId);
            var savedVoucher = voucherRepository.save(voucher);
            return Result.success(savedVoucher);
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("already paid")) {
                return Result.failure(VoucherCommandFailure.VOUCHER_ALREADY_PAID);
            }
            if (e.getMessage().contains("canceled")) {
                return Result.failure(VoucherCommandFailure.VOUCHER_CANCELED);
            }
            if (e.getMessage().contains("exceeds")) {
                return Result.failure(VoucherCommandFailure.PAYMENT_EXCEEDS_TOTAL_DEBT);
            }
            return Result.failure(VoucherCommandFailure.INVALID_VOUCHER_DATA);
        } catch (IllegalArgumentException e) {
            return Result.failure(VoucherCommandFailure.INVALID_VOUCHER_DATA);
        }
    }
}
