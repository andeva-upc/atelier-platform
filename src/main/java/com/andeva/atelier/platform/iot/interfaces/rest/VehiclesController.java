package com.andeva.atelier.platform.iot.interfaces.rest;

import com.andeva.atelier.platform.iot.application.queryservices.VehicleQueryService;
import com.andeva.atelier.platform.iot.domain.model.queries.GetVehiclesAvailableForLinkingQuery;
import com.andeva.atelier.platform.iot.interfaces.rest.resources.VehicleResource;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.VehicleResourceFromAggregateAssembler;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing Vehicle operations inside the iot context.
 */
@RestController
@RequestMapping(value = "/api/v1/vehicles", produces = "application/json")
@Tag(name = "Vehicles", description = "Endpoints for managing vehicle fleet inside the iot context")
public class VehiclesController {

    private final VehicleQueryService vehicleQueryService;

    public VehiclesController(VehicleQueryService vehicleQueryService) {
        this.vehicleQueryService = vehicleQueryService;
    }

    /**
     * Retrieves all vehicles available for linking (unlinked) under a specific branch.
     * @param branchId the branch identifier to filter vehicles
     * @return a ResponseEntity containing the list of vehicle resources
     */
    @GetMapping("/available-for-linking")
    @Operation(summary = "Get vehicles available for linking", description = "Retrieves all vehicles available for linking (unlinked) under a specific branch")
    public ResponseEntity<List<VehicleResource>> getVehiclesAvailableForLinking(@RequestParam UUID branchId) {
        var query = new GetVehiclesAvailableForLinkingQuery(new BranchId(branchId));
        var list = vehicleQueryService.handle(query);
        var resources = list.stream()
                .map(VehicleResourceFromAggregateAssembler::toResourceFromAggregate)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
