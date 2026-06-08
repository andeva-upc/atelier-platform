package com.andeva.atelier.platform.iot.application.internal.queryservices;

import com.andeva.atelier.platform.iot.application.queryservices.TelemetryQueryService;
import com.andeva.atelier.platform.iot.domain.model.aggregates.Obd2DeviceRegistration;
import com.andeva.atelier.platform.iot.domain.model.aggregates.TelemetrySnapshot;
import com.andeva.atelier.platform.iot.domain.model.queries.GetLatestTelemetrySnapshotQuery;
import com.andeva.atelier.platform.iot.domain.model.queries.GetTelemetrySnapshotHistoryQuery;
import com.andeva.atelier.platform.iot.domain.repositories.Obd2DeviceRegistrationRepository;
import com.andeva.atelier.platform.iot.domain.repositories.TelemetrySnapshotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for handling telemetry queries.
 */
@Service
public class TelemetryQueryServiceImpl implements TelemetryQueryService {

    private final TelemetrySnapshotRepository telemetrySnapshotRepository;
    private final Obd2DeviceRegistrationRepository obd2DeviceRegistrationRepository;

    public TelemetryQueryServiceImpl(
            TelemetrySnapshotRepository telemetrySnapshotRepository,
            Obd2DeviceRegistrationRepository obd2DeviceRegistrationRepository
    ) {
        this.telemetrySnapshotRepository = telemetrySnapshotRepository;
        this.obd2DeviceRegistrationRepository = obd2DeviceRegistrationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TelemetrySnapshot> handle(GetLatestTelemetrySnapshotQuery query) {
        return obd2DeviceRegistrationRepository
                .findActiveByObd2DeviceId(query.obd2DeviceId())
                .flatMap(registration -> telemetrySnapshotRepository.findLatestByRegistrationId(registration.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TelemetrySnapshot> handle(GetTelemetrySnapshotHistoryQuery query) {
        return obd2DeviceRegistrationRepository
                .findActiveByObd2DeviceId(query.obd2DeviceId())
                .map(registration -> telemetrySnapshotRepository.findAllByRegistrationId(registration.getId()))
                .orElse(Collections.emptyList());
    }
}
