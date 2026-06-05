package com.andeva.atelier.platform.core.domain.model.commands;

import java.util.UUID;

public record UpdateCustomerCommand(
        UUID userId,
        String firstName,
        String lastName,
        String businessName,
        String documentType,
        String documentNumber,
        String phone
) {
    public UpdateCustomerCommand {
        if (userId == null) throw new IllegalArgumentException("core.error.userId.required");
        if (documentType == null || documentType.isBlank()) throw new IllegalArgumentException("core.error.documentType.required");
        if (documentNumber == null || documentNumber.isBlank()) throw new IllegalArgumentException("core.error.documentNumber.required");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("core.error.phone.required");
    }
}
