package com.andeva.atelier.platform.iot.domain.repositories;

import com.andeva.atelier.platform.iot.domain.model.aggregates.VehicleRegistration;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.VehicleId;

import java.util.Optional;

/**
 * Domain repository interface/port for managing VehicleRegistration operations inside the iot context.
 */
public interface VehicleRegistrationRepository {

    /**
     * Saves a VehicleRegistration aggregate.
     * @param registration the aggregate to save
     * @return the saved aggregate
     */
    VehicleRegistration save(VehicleRegistration registration);

    /**
     * Finds the active registration for a specific vehicle.
     * @param vehicleId the unique identifier of the vehicle
     * @return an Optional containing the active registration if found
     */
    Optional<VehicleRegistration> findActiveByVehicleId(VehicleId vehicleId);
}
