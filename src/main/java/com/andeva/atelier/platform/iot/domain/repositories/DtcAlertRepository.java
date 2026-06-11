package com.andeva.atelier.platform.iot.domain.repositories;

import com.andeva.atelier.platform.iot.domain.model.aggregates.DtcAlert;
import com.andeva.atelier.platform.iot.domain.model.valueobjects.Obd2DeviceRegistrationId;

import java.util.List;

/**
 * Domain repository interface/port for DtcAlert aggregates.
 */
public interface DtcAlertRepository {

    /**
     * Finds all DTC alerts for a specific OBD2 device registration.
     * @param registrationId the registration ID
     * @return the list of DTC alerts ordered by creation date descending
     */
    List<DtcAlert> findAllByRegistrationId(Obd2DeviceRegistrationId registrationId);
}
