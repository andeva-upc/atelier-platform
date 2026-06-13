package com.andeva.atelier.platform.iot.interfaces.rest;

import com.andeva.atelier.platform.iot.application.commandservices.TelemetryCommandService;
import com.andeva.atelier.platform.iot.application.queryservices.TelemetryQueryService;
import com.andeva.atelier.platform.iot.domain.model.queries.GetLatestTelemetrySnapshotQuery;
import com.andeva.atelier.platform.iot.domain.model.queries.GetTelemetrySnapshotHistoryQuery;
import com.andeva.atelier.platform.iot.domain.model.valueobjects.Obd2DeviceId;
import com.andeva.atelier.platform.iot.interfaces.rest.resources.IngestTelemetryBatchResource;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.ResponseEntityFromTelemetryCommandResultAssembler;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.TelemetryCommandFromResourceAssembler;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.TelemetrySnapshotResourceFromAggregateAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for managing vehicle telemetry snapshot ingestion and retrieval.
 */
@RestController
@RequestMapping(value = "/api/v1/vh_telemetry_batches", produces = "application/json")
@Tag(name = "Telemetry Batches", description = "Endpoints for managing OBD2 telemetry batches")
public class VhTelemetryBatchesController {

    private final TelemetryCommandService commandService;
    private final TelemetryQueryService queryService;
    private final MessageSource messageSource;

    public VhTelemetryBatchesController(TelemetryCommandService commandService, TelemetryQueryService queryService, MessageSource messageSource) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.messageSource = messageSource;
    }


    @PostMapping
    @Operation(summary = "Ingest a batch of telemetry snapshots", description = "Ingests a new batch of telemetry snapshots from an OBD2 device")
    public ResponseEntity<?> ingestTelemetryBatch(@Valid @RequestBody IngestTelemetryBatchResource resource) {
        var command = TelemetryCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);
        return ResponseEntityFromTelemetryCommandResultAssembler.toResponseEntityFromResult(result, messageSource);
    }

    @GetMapping("/latest/{deviceId}")
    @Operation(summary = "Get the latest telemetry snapshot for a device", description = "Retrieves the most recent telemetry capture from a specific OBD2 device")
    public ResponseEntity<?> getLatestTelemetrySnapshot(@PathVariable UUID deviceId) {
        var query = new GetLatestTelemetrySnapshotQuery(new Obd2DeviceId(deviceId));
        var result = queryService.handle(query);

        return result.map(snapshot -> ResponseEntity.ok(TelemetrySnapshotResourceFromAggregateAssembler.toResourceFromAggregate(snapshot)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/history/{deviceId}")
    @Operation(summary = "Get the historical telemetry snapshots for a device", description = "Retrieves a history of all telemetry snapshots recorded for a specific OBD2 device ordered descending by date")
    public ResponseEntity<?> getTelemetrySnapshotHistory(@PathVariable UUID deviceId) {
        var query = new GetTelemetrySnapshotHistoryQuery(new Obd2DeviceId(deviceId));
        var list = queryService.handle(query);

        var resources = list.stream()
                .map(TelemetrySnapshotResourceFromAggregateAssembler::toResourceFromAggregate)
                .toList();

        return ResponseEntity.ok(resources);
    }
}
