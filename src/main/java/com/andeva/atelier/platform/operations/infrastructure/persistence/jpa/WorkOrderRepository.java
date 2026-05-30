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

/**
 * Repository interface for managing WorkOrder entities in the database. This interface extends JpaRepository, providing basic CRUD operations and custom query methods for retrieving Work Orders based on various criteria such as Branch ID, Appointment ID, Customer ID, Vehicle ID, and Internal Number. The repository also includes a custom query to find the maximum internal number used by work orders in a specific branch, which is useful for generating sequential internal numbers for new work orders.
 * @author Joel Huamani Estefanero
 */
@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, UUID> {

    /**
     * Found the list of Work Orders by Branch Id
     * @param branchId the Branch Id to search for Work Orders
     * @return a list of Work Orders associated with the given Branch Id
     */
    List<WorkOrder> findAllByBranchId(BranchId branchId);

    /**
     * Found the Work Order by Appointment Id
     * @param appointmentId the Appointment Id to search for the Work Order
     * @return an Optional containing the Work Order associated with the given Appointment Id, or empty if not found
     */
    Optional<WorkOrder> findByAppointmentId(AppointmentId appointmentId);

    /**
     * Check if a Work Order exists by Appointment Id
     * @param appointmentId the Appointment Id to check for the existence of a Work Order
     * @return true if a Work Order exists with the given Appointment Id, false otherwise
     */
    boolean existsByAppointmentId(AppointmentId appointmentId);

    /**
     * Found the list of Work Orders by Customer Id
     * @param customerId the Customer Id to search for Work Orders
     * @return a list of Work Orders associated with the given Customer Id
     */
    List<WorkOrder> findAllByCustomerId(CustomerId customerId);

    /**
     * Found the list of Work Orders by Vehicle Id
     * @param vehicleId the Vehicle Id to search for Work Orders
     * @return a list of Work Orders associated with the given Vehicle Id
     */
    List<WorkOrder> findAllByVehicleId(VehicleId vehicleId);

    /**
     * Found the Work Order by Internal Number and Branch Id
     * @param internalNumber the Internal Number to search for the Work Order
     * @param branchId the Branch Id to search for the Work Order
     * @return an Optional containing the Work Order associated with the given Internal Number and Branch Id, or empty if not found
     */
    Optional<WorkOrder> findByInternalNumberAndBranchId(Integer internalNumber, BranchId branchId);

    /**
     * Finds the maximum internal number used by work orders in a given branch.
     * Used for automatic sequential number generation.
     * @param branchId the Branch Id to check
     * @return the highest internal number, or 0 if no work orders exist in that branch yet
     */
    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(MAX(w.internalNumber), 0) FROM WorkOrder w WHERE w.branchId = :branchId")
    int findMaxInternalNumberByBranchId(@org.springframework.data.repository.query.Param("branchId") BranchId branchId);
}
