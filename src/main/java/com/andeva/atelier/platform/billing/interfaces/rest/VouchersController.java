package com.andeva.atelier.platform.billing.interfaces.rest;

import com.andeva.atelier.platform.billing.application.commandservices.VoucherCommandFailure;
import com.andeva.atelier.platform.billing.application.commandservices.VoucherCommandService;
import com.andeva.atelier.platform.billing.interfaces.rest.resources.GenerateVoucherResource;
import com.andeva.atelier.platform.billing.interfaces.rest.resources.VoucherResource;
import com.andeva.atelier.platform.billing.interfaces.rest.transform.GenerateVoucherCommandFromResourceAssembler;
import com.andeva.atelier.platform.billing.interfaces.rest.transform.VoucherResourceFromAggregateAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/vouchers", produces = "application/json")
@Tag(name = "Vouchers", description = "Endpoints for generating and managing billing vouchers (invoices and receipts)")
public class VouchersController {

    private final VoucherCommandService commandService;

    public VouchersController(VoucherCommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping
    @Operation(summary = "Generate a new voucher", description = "Generates a new Voucher (Invoice/Receipt) based on an Approved Quote and sends it to SUNAT via Facthub")
    public ResponseEntity<?> generateVoucher(@Valid @RequestBody GenerateVoucherResource resource) {
        var command = GenerateVoucherCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        
        if (result.isSuccess()) {
            var voucherResource = VoucherResourceFromAggregateAssembler.toResourceFromAggregate(result.success().get());
            return new ResponseEntity<>(voucherResource, HttpStatus.CREATED);
        }
        
        return toErrorResponse(result.failure().get());
    }

    private ResponseEntity<?> toErrorResponse(VoucherCommandFailure failure) {
        return switch (failure) {
            case QUOTE_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Quote not found");
            case QUOTE_NOT_APPROVED -> ResponseEntity.status(HttpStatus.CONFLICT).body("Quote must be APPROVED to generate a voucher");
            case INVALID_VOUCHER_DATA -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid voucher data");
            case ISSUER_NOT_FOUND -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not determine issuer RUC");
            case FACTHUB_ISSUANCE_FAILED -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Failed to issue voucher via Facthub");
        };
    }
}
