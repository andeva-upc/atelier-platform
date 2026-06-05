package com.andeva.atelier.platform.core.application.queryservices;

import com.andeva.atelier.platform.core.domain.model.queries.GetProfileRolesByUserIdQuery;

import java.util.List;

public interface ProfileQueryService {
    List<String> handle(GetProfileRolesByUserIdQuery query);
}
