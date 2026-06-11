package com.andeva.atelier.platform.iot.application.queryservices;

import com.andeva.atelier.platform.iot.domain.model.aggregates.DtcAlert;
import com.andeva.atelier.platform.iot.domain.model.queries.GetDtcAlertsByRegistrationIdQuery;

import java.util.List;

/**
 * Service interface for handling DTC Alert queries inside the iot context.
 */
public interface DtcAlertQueryService {

    /**
     * Handles retrieving all DTC alerts for a specific OBD2 device registration.
     * @param query the query containing the registration ID
     * @return the list of DTC alerts ordered by creation date descending
     */
    List<DtcAlert> handle(GetDtcAlertsByRegistrationIdQuery query);
}
