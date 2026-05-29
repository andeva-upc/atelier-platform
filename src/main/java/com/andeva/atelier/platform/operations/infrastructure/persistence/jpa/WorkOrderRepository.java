package com.andeva.atelier.platform.operations.infrastructure.persistence.jpa;

import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrder;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.CustomerId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, UUID> {
    /**
     * Recupera todas las órdenes de trabajo de una sucursal específica (Multi-tenant).
     */
    List<WorkOrder> findAllByBranchId(BranchId branchId);
    /**
     * Busca una orden de trabajo por su cita asociada.
     * Útil para validar que no se creen dos órdenes para una misma cita.
     */
    Optional<WorkOrder> findByAppointmentId(AppointmentId appointmentId);
    /**
     * Verifica si ya existe una orden de trabajo para una cita.
     */
    boolean existsByAppointmentId(AppointmentId appointmentId);
    /**
     * Recupera el historial de órdenes de trabajo de un cliente específico.
     */
    List<WorkOrder> findAllByCustomerId(CustomerId customerId);
    /**
     * Recupera el historial de servicios de un vehículo específico.
     */
    List<WorkOrder> findAllByVehicleId(VehicleId vehicleId);
    /**
     * Busca una orden de trabajo específica usando su número correlativo interno
     * dentro de una sucursal específica.
     */
    Optional<WorkOrder> findByInternalNumberAndBranchId(Integer internalNumber, BranchId branchId);
}
