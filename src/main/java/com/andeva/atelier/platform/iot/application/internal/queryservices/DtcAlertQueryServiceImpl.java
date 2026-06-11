package com.andeva.atelier.platform.iot.application.internal.queryservices;

import com.andeva.atelier.platform.iot.application.queryservices.DtcAlertQueryService;
import com.andeva.atelier.platform.iot.domain.model.aggregates.DtcAlert;
import com.andeva.atelier.platform.iot.domain.model.queries.GetDtcAlertsByRegistrationIdQuery;
import com.andeva.atelier.platform.iot.domain.repositories.DtcAlertRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for handling DTC Alert queries inside the iot context.
 */
@Service
public class DtcAlertQueryServiceImpl implements DtcAlertQueryService {

    private final DtcAlertRepository dtcAlertRepository;

    public DtcAlertQueryServiceImpl(DtcAlertRepository dtcAlertRepository) {
        this.dtcAlertRepository = dtcAlertRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtcAlert> handle(GetDtcAlertsByRegistrationIdQuery query) {
        return dtcAlertRepository.findAllByRegistrationId(query.obd2DeviceRegistrationId());
    }
}
