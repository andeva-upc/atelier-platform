package com.andeva.atelier.platform.billing.application.internal.queryservices;

import com.andeva.atelier.platform.billing.application.queryservices.QuoteQueryService;
import com.andeva.atelier.platform.billing.domain.model.aggregates.Quote;
import com.andeva.atelier.platform.billing.domain.model.queries.GetQuoteByIdQuery;
import com.andeva.atelier.platform.billing.domain.repositories.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the QuoteQueryService interface.
 * Delegates query operations to the underlying Quote persistence repository.
 */
@Service
public class QuoteQueryServiceImpl implements QuoteQueryService {

    private final QuoteRepository quoteRepository;

    public QuoteQueryServiceImpl(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    @Override
    public Optional<Quote> handle(GetQuoteByIdQuery query) {
        return quoteRepository.findById(query.quoteId());
    }
}
