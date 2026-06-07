package com.andeva.atelier.platform.billing.interfaces.rest;

import com.andeva.atelier.platform.billing.application.commandservices.QuoteCommandFailure;
import com.andeva.atelier.platform.billing.application.commandservices.QuoteCommandService;
import com.andeva.atelier.platform.billing.domain.model.aggregates.Quote;
import com.andeva.atelier.platform.billing.interfaces.rest.resources.CreateQuoteResource;
import com.andeva.atelier.platform.billing.interfaces.rest.transform.CreateQuoteCommandFromResourceAssembler;
import com.andeva.atelier.platform.billing.interfaces.rest.transform.QuoteResourceFromAggregateAssembler;
import com.andeva.atelier.platform.shared.application.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing billing quotes.
 * This controller provides endpoints for creating and interacting with quotes.
 * It delegates command handling to the QuoteCommandService.
 */
@RestController
@RequestMapping(value = "/api/v1/quotes", produces = "application/json")
@Tag(name = "Quotes", description = "Endpoints for managing billing quotes")
public class QuotesController {

    private final QuoteCommandService commandService;

    public QuotesController(QuoteCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    @Operation(summary = "Create a new quote", description = "Creates a new Quote based on a Work Order")
    public ResponseEntity<?> createQuote(@Valid @RequestBody CreateQuoteResource resource) {
        var command = CreateQuoteCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return toResponse(result);
    }

    private ResponseEntity<?> toResponse(Result<Quote, QuoteCommandFailure> result) {
        if (result.isSuccess()) {
            var resource = QuoteResourceFromAggregateAssembler.toResourceFromAggregate(result.success().get());
            return new ResponseEntity<>(resource, HttpStatus.CREATED);
        }

        return switch (result.failure().get()) {
            case WORK_ORDER_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Work order not found");
            case INVALID_QUOTE_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid quote data");
            case QUOTE_ALREADY_EXISTS_FOR_WORK_ORDER -> ResponseEntity.status(HttpStatus.CONFLICT).body("Quote already exists");
        };
    }
}
