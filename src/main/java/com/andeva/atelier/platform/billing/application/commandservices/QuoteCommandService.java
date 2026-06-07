package com.andeva.atelier.platform.billing.application.commandservices;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Quote;
import com.andeva.atelier.platform.billing.domain.model.commands.CreateQuoteCommand;
import com.andeva.atelier.platform.shared.application.result.Result;

/**
 * Application service contract for executing Quote command operations.
 * Returns a Result mapping either the successfully created aggregate or a specific business failure.
 */
public interface QuoteCommandService {
    
    /**
     * Creates a new Quote based on an existing Work Order.
     * Validates the existence of the Work Order and calculates the total amount considering the given discount.
     * 
     * @param command The command object containing the work order ID, branch ID, and discount percentage.
     * @return A Result containing the created Quote if successful, or a failure reason otherwise.
     */
    Result<Quote, QuoteCommandFailure> handle(CreateQuoteCommand command);
}
