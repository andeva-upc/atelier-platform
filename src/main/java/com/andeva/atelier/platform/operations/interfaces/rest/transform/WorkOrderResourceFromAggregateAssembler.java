package com.andeva.atelier.platform.operations.interfaces.rest.transform;

import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrder;
import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrderTask;
import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrderTaskProduct;
import com.andeva.atelier.platform.operations.interfaces.rest.resources.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Assembler class to translate Work Order domain aggregates/entities to REST resources.
 */
public final class WorkOrderResourceFromAggregateAssembler {

    private WorkOrderResourceFromAggregateAssembler() {}

    /**
     * Convierte el Aggregate Root WorkOrder completo a su recurso de respuesta REST.
     */
    public static WorkOrderResource toResourceFromAggregate(WorkOrder workOrder) {
        List<WorkOrderTaskResource> taskResources = new ArrayList<>();

        for (WorkOrderTask task : workOrder.getTasks()) {
            if (!task.isDeleted()) {
                taskResources.add(toResourceFromTask(task));
            }
        }

        return new WorkOrderResource(
                workOrder.getId(),
                workOrder.getAppointmentId().value(),
                workOrder.getBranchId().value(),
                workOrder.getVehicleId().value(),
                workOrder.getCustomerId().value(),
                workOrder.getInternalNumber(),
                workOrder.getStatus().name(),
                workOrder.getDiagnosticSummary().value(),
                workOrder.getMileageIn().value(),
                workOrder.getTotalAmount().amount(),
                taskResources,
                workOrder.getCreatedAt(),
                workOrder.getUpdatedAt()
        );
    }

    /**
     * Convierte una entidad interna WorkOrderTask a su recurso REST.
     */
    private static WorkOrderTaskResource toResourceFromTask(WorkOrderTask task) {
        List<WorkOrderTaskProductResource> productResources = new ArrayList<>();

        for (WorkOrderTaskProduct product : task.getProducts()) {
            if (!product.isDeleted()) {
                productResources.add(toResourceFromProduct(product));
            }
        }

        return new WorkOrderTaskResource(
                task.getId(),
                task.getServiceId().value(),
                task.getBranchId().value(),
                task.getAssignedMechanicId().value(),
                task.getStatus().name(),
                task.getDescription().value(),
                task.getPrice().amount(),
                task.getStartedAt(),
                task.getCompletedAt(),
                productResources,
                task.getCreatedAt()
        );
    }

    /**
     * Convierte una entidad interna WorkOrderTaskProduct a su recurso REST.
     */
    private static WorkOrderTaskProductResource toResourceFromProduct(WorkOrderTaskProduct product) {
        return new WorkOrderTaskProductResource(
                product.getId(),
                product.getProductId().value(),
                product.getBranchId().value(),
                product.getQuantity().value(),
                product.getUnitPrice().amount(),
                product.getTotalAmount().amount(),
                product.getCreatedAt()
        );
    }
}