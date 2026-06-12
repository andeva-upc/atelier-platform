package com.andeva.atelier.platform.fleet.application.internal.queryservices;

import com.andeva.atelier.platform.fleet.application.queryservices.AppointmentQueryFailure;
import com.andeva.atelier.platform.fleet.application.queryservices.AppointmentQueryService;
import com.andeva.atelier.platform.fleet.domain.model.aggregates.Appointment;
import com.andeva.atelier.platform.fleet.domain.repositories.AppointmentRepository;
import com.andeva.atelier.platform.shared.application.result.Result;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentQueryServiceImpl implements AppointmentQueryService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentQueryServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Result<List<Appointment>, AppointmentQueryFailure> handle(BranchId branchId) {
        try {
            var appointments = appointmentRepository.findByBranchId(branchId);
            return Result.success(appointments);
        } catch (IllegalArgumentException e) {
            return Result.failure(AppointmentQueryFailure.INVALID_QUERY_PARAMS);
        }
    }
}
