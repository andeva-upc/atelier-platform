package com.andeva.atelier.platform.operations.application.internal.queryservices;

import com.andeva.atelier.platform.operations.application.queryservices.WorkOrderQueryService;
import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrder;
import com.andeva.atelier.platform.operations.domain.model.queries.*;
import com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.WorkOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Internal application service implementing {@link WorkOrderQueryService}.
 * Provides read-only transactional access to work order aggregates.
 */
@Service
@Transactional(readOnly = true) // Optimiza las conexiones de lectura de la base de datos
public class WorkOrderQueryServiceImpl implements WorkOrderQueryService {

    private final WorkOrderRepository workOrderRepository;

    // Inyección de dependencias por constructor
    public WorkOrderQueryServiceImpl(WorkOrderRepository workOrderRepository) {
        this.workOrderRepository = workOrderRepository;
    }

    @Override
    public Optional<WorkOrder> handle(GetWorkOrderByIdQuery query) {
        if (query.workOrderId() == null) {
            throw new IllegalArgumentException("operations.error.query.workOrderId.required");
        }
        return workOrderRepository.findById(query.workOrderId());
    }

    @Override
    public List<WorkOrder> handle(GetWorkOrdersByBranchIdQuery query) {
        if (query.branchId() == null) {
            throw new IllegalArgumentException("operations.error.query.branchId.required");
        }
        return workOrderRepository.findAllByBranchId(query.branchId());
    }

    @Override
    public List<WorkOrder> handle(GetWorkOrdersByVehicleIdQuery query) {
        if (query.vehicleId() == null) {
            throw new IllegalArgumentException("operations.error.query.vehicleId.required");
        }
        return workOrderRepository.findAllByVehicleId(query.vehicleId());
    }
}