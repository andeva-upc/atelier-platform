package com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrder;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.AppointmentId;
import com.andeva.atelier.platform.operations.domain.repositories.WorkOrderRepository;
import com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.assemblers.WorkOrderPersistenceAssembler;
import com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.entities.WorkOrderPersistenceEntity;
import com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.repositories.WorkOrderPersistenceRepository;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.CustomerId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.VehicleId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class WorkOrderRepositoryImpl implements WorkOrderRepository {

    private final WorkOrderPersistenceRepository persistenceRepository;
    private final ApplicationEventPublisher eventPublisher;

    public WorkOrderRepositoryImpl(WorkOrderPersistenceRepository persistenceRepository, ApplicationEventPublisher eventPublisher) {
        this.persistenceRepository = persistenceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public WorkOrder save(WorkOrder workOrder) {
        WorkOrderPersistenceEntity entity = WorkOrderPersistenceAssembler.toPersistenceEntity(workOrder);
        WorkOrderPersistenceEntity savedEntity = persistenceRepository.save(entity);
        WorkOrder savedWorkOrder = WorkOrderPersistenceAssembler.toDomainEntity(savedEntity);
        
        // Publish the domain events that were registered in the original domain aggregate
        workOrder.domainEvents().forEach(eventPublisher::publishEvent);
        workOrder.clearDomainEvents();
        
        return savedWorkOrder;
    }

    @Override
    public Optional<WorkOrder> findById(UUID id) {
        return persistenceRepository.findById(id)
                .map(WorkOrderPersistenceAssembler::toDomainEntity);
    }

    @Override
    public List<WorkOrder> findAllByBranchId(BranchId branchId) {
        return persistenceRepository.findAllByBranchId(branchId).stream()
                .map(WorkOrderPersistenceAssembler::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WorkOrder> findByAppointmentId(AppointmentId appointmentId) {
        return persistenceRepository.findByAppointmentId(appointmentId)
                .map(WorkOrderPersistenceAssembler::toDomainEntity);
    }

    @Override
    public boolean existsByAppointmentId(AppointmentId appointmentId) {
        return persistenceRepository.existsByAppointmentId(appointmentId);
    }

    @Override
    public List<WorkOrder> findAllByCustomerId(CustomerId customerId) {
        return persistenceRepository.findAllByCustomerId(customerId).stream()
                .map(WorkOrderPersistenceAssembler::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<WorkOrder> findAllByVehicleId(VehicleId vehicleId) {
        return persistenceRepository.findAllByVehicleId(vehicleId).stream()
                .map(WorkOrderPersistenceAssembler::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WorkOrder> findByInternalNumberAndBranchId(Integer internalNumber, BranchId branchId) {
        return persistenceRepository.findByInternalNumberAndBranchId(internalNumber, branchId)
                .map(WorkOrderPersistenceAssembler::toDomainEntity);
    }

    @Override
    public int findMaxInternalNumberByBranchId(BranchId branchId) {
        return persistenceRepository.findMaxInternalNumberByBranchId(branchId);
    }
}
