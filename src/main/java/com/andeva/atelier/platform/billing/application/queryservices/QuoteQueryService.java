package com.andeva.atelier.platform.billing.application.queryservices;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Quote;
import com.andeva.atelier.platform.billing.domain.model.queries.GetQuoteByIdQuery;

import java.util.Optional;

/**
 * Application service contract for executing Quote query operations.
 * Handles read-only operations related to Quotes.
 */
public interface QuoteQueryService {
    
    /**
     * Handles the retrieval of a Quote by its unique identifier.
     * 
     * @param query The query object containing the quote ID.
     * @return An Optional containing the Quote if found, or empty otherwise.
     */
    Optional<Quote> handle(GetQuoteByIdQuery query);
}
