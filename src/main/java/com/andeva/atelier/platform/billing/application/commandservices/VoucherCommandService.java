package com.andeva.atelier.platform.billing.application.commandservices;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Voucher;
import com.andeva.atelier.platform.billing.domain.model.commands.GenerateVoucherCommand;
import com.andeva.atelier.platform.shared.application.result.Result;
import com.andeva.atelier.platform.billing.domain.model.valueobjects.VoucherCommandFailure;

public interface VoucherCommandService {
    Result<Voucher, VoucherCommandFailure> handle(GenerateVoucherCommand command);
}
