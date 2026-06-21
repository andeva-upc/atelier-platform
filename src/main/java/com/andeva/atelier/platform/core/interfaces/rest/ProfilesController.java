package com.andeva.atelier.platform.core.interfaces.rest;

import com.andeva.atelier.platform.core.application.queryservices.ProfileQueryService;
import com.andeva.atelier.platform.core.domain.model.queries.GetProfileRolesByUserIdQuery;
import com.andeva.atelier.platform.core.domain.model.valueobjects.UserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "Profiles", description = "Operations related to user profiles")
public class ProfilesController {

    private final ProfileQueryService profileQueryService;

    public ProfilesController(ProfileQueryService profileQueryService) {
        this.profileQueryService = profileQueryService;
    }

    @GetMapping("/roles")
    @Operation(summary = "Get all profile roles for a specific user ID", description = "Returns a list of roles (e.g. OWNER, CUSTOMER, EMPLOYEE) that the user currently has.")
    public ResponseEntity<List<String>> getUserProfileRoles(@RequestParam(name = "userId") UUID userId) {
        var query = new GetProfileRolesByUserIdQuery(new UserId(userId));
        var roles = profileQueryService.handle(query);
        return ResponseEntity.ok(roles);
    }
}
