package com.andeva.atelier.platform.billing.application.commandservices;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Voucher;
import com.andeva.atelier.platform.billing.domain.model.commands.GenerateVoucherCommand;
import com.andeva.atelier.platform.shared.application.result.Result;
import com.andeva.atelier.platform.billing.domain.model.valueobjects.VoucherCommandFailure;
import com.andeva.atelier.platform.billing.domain.model.commands.AddPaymentCommand;
import com.andeva.atelier.platform.billing.domain.model.commands.RemovePaymentCommand;
import com.andeva.atelier.platform.billing.domain.model.commands.ProcessCheckoutCommand;

public interface VoucherCommandService {
    Result<Voucher, VoucherCommandFailure> handle(GenerateVoucherCommand command);
    Result<Voucher, VoucherCommandFailure> handle(AddPaymentCommand command);
    Result<Voucher, VoucherCommandFailure> handle(RemovePaymentCommand command);
    Result<Voucher, VoucherCommandFailure> handle(ProcessCheckoutCommand command);
}
