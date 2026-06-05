package com.andeva.atelier.platform.core.domain.model.queries;

import java.util.UUID;

public record GetProfileRolesByUserIdQuery(UUID userId) {
    public GetProfileRolesByUserIdQuery {
        if (userId == null) throw new IllegalArgumentException("core.error.userId.required");
    }
}
