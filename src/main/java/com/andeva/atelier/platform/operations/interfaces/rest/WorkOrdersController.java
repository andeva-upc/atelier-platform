package com.andeva.atelier.platform.operations.interfaces.rest;

import com.andeva.atelier.platform.operations.application.commandservices.WorkOrderCommandFailure;
import com.andeva.atelier.platform.operations.application.commandservices.WorkOrderCommandService;
import com.andeva.atelier.platform.operations.application.queryservices.WorkOrderQueryService;
import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrder;
import com.andeva.atelier.platform.operations.domain.model.commands.*;
import com.andeva.atelier.platform.operations.domain.model.queries.*;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.ProductId;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.WorkOrderId;
import com.andeva.atelier.platform.operations.domain.model.valueobjects.WorkOrderTaskId;
import com.andeva.atelier.platform.operations.interfaces.rest.resources.*;
import com.andeva.atelier.platform.operations.interfaces.rest.transform.*;
import com.andeva.atelier.platform.shared.application.result.Result;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.VehicleId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing workshop work orders and mechanic tasks. This controller provides endpoints for creating, updating, retrieving, and deleting work orders, as well as managing the tasks and products associated with those work orders. It uses a command-query separation approach, delegating command handling to the WorkOrderCommandService and query handling to the WorkOrderQueryService. The controller also utilizes a MessageSource for internationalization of response messages.
 * @author Joel Huamani Estefanero
 */
@RestController
@RequestMapping(value = "/api/v1/work-orders", produces = "application/json")
@Tag(name = "Work Orders", description = "Endpoints for managing workshop work orders and mechanic tasks")
public class WorkOrdersController {

    private final WorkOrderCommandService commandService;
    private final WorkOrderQueryService queryService;
    private final MessageSource messageSource;

    public WorkOrdersController(WorkOrderCommandService commandService,
                                WorkOrderQueryService queryService,
                                MessageSource messageSource) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.messageSource = messageSource;
    }

    /**
     * Helper private method to transform command Result mapping branch short code formatting.
     */
    private ResponseEntity<?> toResponse(Result<WorkOrder, WorkOrderCommandFailure> result) {
        String branchCode = result.success()
                .map(wo -> queryService.getBranchCode(wo.getBranchId().value()))
                .orElse("WO");
        return ResponseEntityFromWorkOrderCommandResultAssembler.toResponseEntityFromResult(result, messageSource, branchCode);
    }

    @PostMapping
    @Operation(summary = "Create a new Work Order", description = "Creates a new Work Order for a specific branch and vehicle")
    public ResponseEntity<?> createWorkOrder(@Valid @RequestBody CreateWorkOrderResource resource) {
        var command = WorkOrderCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @PostMapping("/{id}/tasks")
    @Operation(summary = "Add a mechanic task to a Work Order", description = "Adds a new mechanic task to an existing Work Order")
    public ResponseEntity<?> addTaskToWorkOrder(@PathVariable UUID id, @Valid @RequestBody AddTaskResource resource) {
        var command = WorkOrderCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @PutMapping("/{id}/tasks/{taskId}")
    @Operation(summary = "Update mechanic task details", description = "Updates the details of a specific mechanic task")
    public ResponseEntity<?> updateWorkOrderTaskDetails(@PathVariable UUID id, @PathVariable UUID taskId,
                                                        @Valid @RequestBody UpdateWorkOrderTaskDetailsResource resource) {
        var command = WorkOrderCommandFromResourceAssembler.toCommandFromResource(id, taskId, resource);
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @PostMapping("/{id}/tasks/{taskId}/products")
    @Operation(summary = "Add an inventory product/part to a task", description = "Adds a product from inventory to a specific mechanic task")
    public ResponseEntity<?> addProductToTask(@PathVariable UUID id, @PathVariable UUID taskId,
                                              @Valid @RequestBody AddProductResource resource) {
        var command = WorkOrderCommandFromResourceAssembler.toCommandFromResource(id, taskId, resource);
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @PutMapping("/{id}/tasks/{taskId}/products/{productId}")
    @Operation(summary = "Update a product's quantity in a task", description = "Updates the quantity of a product used in a specific mechanic task")
    public ResponseEntity<?> updateProductQuantityInTask(@PathVariable UUID id, @PathVariable UUID taskId,
                                                         @PathVariable UUID productId,
                                                         @Valid @RequestBody UpdateProductQuantityInTaskResource resource) {
        var command = WorkOrderCommandFromResourceAssembler.toCommandFromResource(id, taskId, productId, resource);
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @DeleteMapping("/{id}/tasks/{taskId}/products/{productId}")
    @Operation(summary = "Remove a product/part from a task", description = "Removes a product from a task, releasing its stock reservation")
    public ResponseEntity<?> removeProductFromTask(@PathVariable UUID id, @PathVariable UUID taskId,
                                                   @PathVariable UUID productId) {
        var command = new RemoveProductFromTaskCommand(new WorkOrderId(id), new WorkOrderTaskId(taskId), new ProductId(productId));
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @DeleteMapping("/{id}/tasks/{taskId}")
    @Operation(summary = "Remove a task from the Work Order", description = "Removes a task from the Work Order, releasing all its stock reservations")
    public ResponseEntity<?> removeTaskFromWorkOrder(@PathVariable UUID id, @PathVariable UUID taskId) {
        var command = new RemoveTaskFromWorkOrderCommand(new WorkOrderId(id), new WorkOrderTaskId(taskId));
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a Work Order", description = "Soft deletes a Work Order, releasing all active stock reservations")
    public ResponseEntity<?> deleteWorkOrder(@PathVariable UUID id) {
        var command = new DeleteWorkOrderCommand(new WorkOrderId(id));
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @PostMapping("/{id}/tasks/{taskId}/start")
    @Operation(summary = "Start executing a task", description = "Sets the task status to DOING and captures the startedAt timestamp")
    public ResponseEntity<?> startTask(@PathVariable UUID id, @PathVariable UUID taskId) {
        var command = new StartTaskCommand(new WorkOrderId(id), new WorkOrderTaskId(taskId));
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @PostMapping("/{id}/tasks/{taskId}/complete")
    @Operation(summary = "Complete a task", description = "Sets the task status to COMPLETED and captures the completedAt timestamp")
    public ResponseEntity<?> completeTask(@PathVariable UUID id, @PathVariable UUID taskId) {
        var command = new CompleteTaskCommand(new WorkOrderId(id), new WorkOrderTaskId(taskId));
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @PostMapping("/{id}/tasks/{taskId}/reopen")
    @Operation(summary = "Reopen a completed task", description = "Returns the task to DOING, clears completedAt, and keeps stock reserved")
    public ResponseEntity<?> reopenTask(@PathVariable UUID id, @PathVariable UUID taskId) {
        var command = new ReopenTaskCommand(new WorkOrderId(id), new WorkOrderTaskId(taskId));
        var result = commandService.handle(command);
        return toResponse(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Work Order by ID", description = "Retrieves the details of a specific Work Order")
    public ResponseEntity<?> getWorkOrderById(@PathVariable UUID id) {
        var query = new GetWorkOrderByIdQuery(new WorkOrderId(id));
        var workOrder = queryService.handle(query);

        return workOrder.map(value -> {
            String branchCode = queryService.getBranchCode(value.getBranchId().value());
            return ResponseEntity.ok(WorkOrderResourceFromAggregateAssembler.toResourceFromAggregate(value, branchCode));
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get all Work Orders for a specific branch", description = "Retrieves a list of all Work Orders associated with a specific branch (Multi-tenant query)")
    public ResponseEntity<?> getWorkOrdersByBranch(@PathVariable UUID branchId) {
        var query = new GetWorkOrdersByBranchIdQuery(new BranchId(branchId));
        List<WorkOrder> list = queryService.handle(query);
        String branchCode = queryService.getBranchCode(branchId);
        List<WorkOrderResource> resources = list.stream()
                .map(value -> WorkOrderResourceFromAggregateAssembler.toResourceFromAggregate(value, branchCode))
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get all Work Orders for a specific vehicle", description = "Retrieves the service history (Work Orders) for a specific vehicle")
    public ResponseEntity<?> getWorkOrdersByVehicle(@PathVariable UUID vehicleId) {
        var query = new GetWorkOrdersByVehicleIdQuery(new VehicleId(vehicleId));
        List<WorkOrder> list = queryService.handle(query);
        List<WorkOrderResource> resources = list.stream()
                .map(value -> {
                    String branchCode = queryService.getBranchCode(value.getBranchId().value());
                    return WorkOrderResourceFromAggregateAssembler.toResourceFromAggregate(value, branchCode);
                })
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Work Order details", description = "Updates the diagnostic summary and mileage of a Work Order")
    public ResponseEntity<?> updateWorkOrderDetails(@PathVariable UUID id,
                                                    @Valid @RequestBody UpdateWorkOrderDetailsResource resource) {
        var command = WorkOrderCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var result = commandService.handle(command);
        return toResponse(result);
    }
}