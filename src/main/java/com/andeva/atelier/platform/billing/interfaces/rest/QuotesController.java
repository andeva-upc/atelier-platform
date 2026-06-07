package com.andeva.atelier.platform.billing.interfaces.rest;

import com.andeva.atelier.platform.billing.application.commandservices.QuoteCommandFailure;
import com.andeva.atelier.platform.billing.application.commandservices.QuoteCommandService;
import com.andeva.atelier.platform.billing.domain.model.aggregates.Quote;
import com.andeva.atelier.platform.billing.interfaces.rest.resources.CreateQuoteResource;
import com.andeva.atelier.platform.billing.interfaces.rest.transform.CreateQuoteCommandFromResourceAssembler;
import com.andeva.atelier.platform.billing.interfaces.rest.transform.QuoteResourceFromAggregateAssembler;
import com.andeva.atelier.platform.billing.application.queryservices.QuoteQueryService;
import com.andeva.atelier.platform.billing.domain.model.queries.GetQuoteByIdQuery;
import com.andeva.atelier.platform.billing.domain.model.queries.GetQuotesByBranchIdQuery;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.billing.interfaces.rest.resources.QuoteResource;
import com.andeva.atelier.platform.shared.application.result.Result;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
    private final QuoteQueryService queryService;

    public QuotesController(QuoteCommandService commandService, QuoteQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    /**
     * Handles the creation of a new quote.
     * 
     * @param resource The resource payload containing quote creation details.
     * @return A ResponseEntity with the created quote resource and a 201 CREATED status, 
     *         or an appropriate error status based on the business failure.
     */
    @PostMapping
    @Operation(summary = "Create a new quote", description = "Creates a new Quote based on a Work Order")
    public ResponseEntity<?> createQuote(@Valid @RequestBody CreateQuoteResource resource) {
        var command = CreateQuoteCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return toResponse(result);
    }

    /**
     * Handles the retrieval of a quote by its ID.
     * 
     * @param id The unique identifier of the quote.
     * @return A ResponseEntity with the quote resource and a 200 OK status, 
     *         or a 404 NOT FOUND status if the quote does not exist.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get quote by ID", description = "Retrieves a Quote by its unique identifier")
    public ResponseEntity<QuoteResource> getQuoteById(@PathVariable UUID id) {
        var query = new GetQuoteByIdQuery(id);
        var quote = queryService.handle(query);
        if (quote.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var resource = QuoteResourceFromAggregateAssembler.toResourceFromAggregate(quote.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Handles the retrieval of all quotes associated with a specific branch.
     * 
     * @param branchId The unique identifier of the branch.
     * @return A ResponseEntity containing a list of quote resources and a 200 OK status.
     */
    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get quotes by branch ID", description = "Retrieves all Quotes belonging to a specific branch")
    public ResponseEntity<List<QuoteResource>> getQuotesByBranchId(@PathVariable UUID branchId) {
        var query = new GetQuotesByBranchIdQuery(new BranchId(branchId));
        var quotes = queryService.handle(query);
        var resources = quotes.stream()
                .map(QuoteResourceFromAggregateAssembler::toResourceFromAggregate)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
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
