package com.andeva.atelier.platform.iot.domain.repositories;

import com.andeva.atelier.platform.iot.domain.model.aggregates.Vehicle;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;

import java.util.List;

/**
 * Domain repository interface/port for managing Vehicle operations inside the iot context.
 */
public interface VehicleRepository {

    /**
     * Finds all vehicles in a specific branch that are available for linking (unlinked).
     * @param branchId the unique identifier of the branch
     * @return the list of available vehicles
     */
    List<Vehicle> findAvailableForLinkingByBranchId(BranchId branchId);
}
