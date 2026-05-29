package com.andeva.atelier.platform.operations.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * REST resource representing a Work Order Task Product payload.
 */
public record WorkOrderTaskProductResource(
        UUID id,
        UUID productId,
        UUID branchId,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal totalAmount,
        Instant createdAt
) {}