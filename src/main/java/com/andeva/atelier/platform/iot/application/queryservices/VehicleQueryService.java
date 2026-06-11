package com.andeva.atelier.platform.iot.application.queryservices;

import com.andeva.atelier.platform.iot.domain.model.aggregates.Vehicle;
import com.andeva.atelier.platform.iot.domain.model.queries.GetVehiclesAvailableForLinkingQuery;

import java.util.List;

/**
 * Service interface for handling Vehicle queries inside the iot context.
 */
public interface VehicleQueryService {

    /**
     * Handles retrieving all vehicles available for linking in a branch.
     * @param query the query containing the branch ID
     * @return the list of available vehicles
     */
    List<Vehicle> handle(GetVehiclesAvailableForLinkingQuery query);
}
