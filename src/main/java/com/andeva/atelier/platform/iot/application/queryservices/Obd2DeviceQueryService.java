package com.andeva.atelier.platform.iot.application.queryservices;

import com.andeva.atelier.platform.iot.domain.model.aggregates.Obd2Device;
import com.andeva.atelier.platform.iot.domain.model.queries.GetObd2DeviceByIdQuery;

import java.util.Optional;

/**
 * Service interface for handling OBD2 device queries.
 */
public interface Obd2DeviceQueryService {

    /**
     * Handles retrieving an OBD2 device by its unique ID.
     * @param query the query containing the device ID
     * @return an Optional containing the device if found, or empty
     */
    Optional<Obd2Device> handle(GetObd2DeviceByIdQuery query);
}
