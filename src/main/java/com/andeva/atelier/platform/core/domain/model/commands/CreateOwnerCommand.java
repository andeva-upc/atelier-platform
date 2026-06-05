package com.andeva.atelier.platform.core.domain.model.commands;

import java.util.UUID;

public record CreateOwnerCommand(
        UUID userId,
        String firstName,
        String lastName,
        String documentType,
        String documentNumber,
        String phone
) {
    public CreateOwnerCommand {
        if (userId == null) throw new IllegalArgumentException("core.error.userId.required");
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("core.error.firstName.required");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("core.error.lastName.required");
        if (documentType == null || documentType.isBlank()) throw new IllegalArgumentException("core.error.documentType.required");
        if (documentNumber == null || documentNumber.isBlank()) throw new IllegalArgumentException("core.error.documentNumber.required");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("core.error.phone.required");
    }
}
