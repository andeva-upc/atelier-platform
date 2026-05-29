package com.andeva.atelier.platform.operations.interfaces.rest;

import com.andeva.atelier.platform.operations.application.commandservices.WorkOrderCommandService;
import com.andeva.atelier.platform.operations.application.queryservices.WorkOrderQueryService;
import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrder;
import com.andeva.atelier.platform.operations.domain.model.commands.*;
import com.andeva.atelier.platform.operations.domain.model.queries.*;
import com.andeva.atelier.platform.operations.interfaces.rest.resources.*;
import com.andeva.atelier.platform.operations.interfaces.rest.transform.*;
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

    @PostMapping
    @Operation(summary = "Create a new Work Order")
    public ResponseEntity<?> createWorkOrder(@Valid @RequestBody CreateWorkOrderResource resource) {
        var command = WorkOrderCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityFromWorkOrderCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    @PostMapping("/{id}/tasks")
    @Operation(summary = "Add a mechanic task to a Work Order")
    public ResponseEntity<?> addTaskToWorkOrder(@PathVariable UUID id, @Valid @RequestBody AddTaskResource resource) {
        var command = WorkOrderCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var result = commandService.handle(command);
        return ResponseEntityFromWorkOrderCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    @PostMapping("/{id}/tasks/{taskId}/products")
    @Operation(summary = "Add an inventory product/part to a task")
    public ResponseEntity<?> addProductToTask(@PathVariable UUID id, @PathVariable UUID taskId,
                                              @Valid @RequestBody AddProductResource resource) {
        var command = WorkOrderCommandFromResourceAssembler.toCommandFromResource(id, taskId, resource);
        var result = commandService.handle(command);
        return ResponseEntityFromWorkOrderCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    @DeleteMapping("/{id}/tasks/{taskId}/products/{productId}")
    @Operation(summary = "Remove a product/part from a task (releases stock reservation)")
    public ResponseEntity<?> removeProductFromTask(@PathVariable UUID id, @PathVariable UUID taskId,
                                                   @PathVariable UUID productId) {
        var command = new RemoveProductFromTaskCommand(id, taskId, new com.andeva.atelier.platform.operations.domain.model.valueobjects.ProductId(productId));
        var result = commandService.handle(command);
        return ResponseEntityFromWorkOrderCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    @DeleteMapping("/{id}/tasks/{taskId}")
    @Operation(summary = "Remove a task from the Work Order (releases all task's stock reservations)")
    public ResponseEntity<?> removeTaskFromWorkOrder(@PathVariable UUID id, @PathVariable UUID taskId) {
        var command = new RemoveTaskFromWorkOrderCommand(id, taskId);
        var result = commandService.handle(command);
        return ResponseEntityFromWorkOrderCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    @PostMapping("/{id}/tasks/{taskId}/start")
    @Operation(summary = "Start executing a task (sets status to DOING and captures startedAt)")
    public ResponseEntity<?> startTask(@PathVariable UUID id, @PathVariable UUID taskId) {
        var command = new StartTaskCommand(id, taskId);
        var result = commandService.handle(command);
        return ResponseEntityFromWorkOrderCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    @PostMapping("/{id}/tasks/{taskId}/complete")
    @Operation(summary = "Complete a task (sets status to COMPLETED and captures completedAt)")
    public ResponseEntity<?> completeTask(@PathVariable UUID id, @PathVariable UUID taskId) {
        var command = new CompleteTaskCommand(id, taskId);
        var result = commandService.handle(command);
        return ResponseEntityFromWorkOrderCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    @PostMapping("/{id}/tasks/{taskId}/reopen")
    @Operation(summary = "Reopen a completed task (returns task to DOING, clears completedAt, releases stock)")
    public ResponseEntity<?> reopenTask(@PathVariable UUID id, @PathVariable UUID taskId) {
        var command = new ReopenTaskCommand(id, taskId);
        var result = commandService.handle(command);
        return ResponseEntityFromWorkOrderCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a Work Order by ID")
    public ResponseEntity<?> getWorkOrderById(@PathVariable UUID id) {
        var query = new GetWorkOrderByIdQuery(id);
        var workOrder = queryService.handle(query);

        return workOrder.map(value -> ResponseEntity.ok(WorkOrderResourceFromAggregateAssembler.toResourceFromAggregate(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get all Work Orders for a specific branch (Multi-tenant query)")
    public ResponseEntity<?> getWorkOrdersByBranch(@PathVariable UUID branchId) {
        var query = new GetWorkOrdersByBranchIdQuery(new BranchId(branchId));
        List<WorkOrder> list = queryService.handle(query);
        List<WorkOrderResource> resources = list.stream()
                .map(WorkOrderResourceFromAggregateAssembler::toResourceFromAggregate)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/vehicle/{vehicleId}")
    @Operation(summary = "Get all Work Orders (service history) for a specific vehicle")
    public ResponseEntity<?> getWorkOrdersByVehicle(@PathVariable UUID vehicleId) {
        var query = new GetWorkOrdersByVehicleIdQuery(new VehicleId(vehicleId));
        List<WorkOrder> list = queryService.handle(query);
        List<WorkOrderResource> resources = list.stream()
                .map(WorkOrderResourceFromAggregateAssembler::toResourceFromAggregate)
                .toList();
        return ResponseEntity.ok(resources);
    }
}