package com.andeva.atelier.platform.billing.application.commandservices;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Quote;
import com.andeva.atelier.platform.billing.domain.model.commands.CreateQuoteCommand;
import com.andeva.atelier.platform.shared.application.result.Result;

public interface QuoteCommandService {
    Result<Quote, QuoteCommandFailure> handle(CreateQuoteCommand command);
}
