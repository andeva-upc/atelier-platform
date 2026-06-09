package com.andeva.atelier.platform.iot.interfaces.rest;

import com.andeva.atelier.platform.iot.application.commandservices.TelemetryCommandFailure;
import com.andeva.atelier.platform.iot.application.commandservices.TelemetryCommandService;
import com.andeva.atelier.platform.iot.application.queryservices.TelemetryQueryService;
import com.andeva.atelier.platform.iot.domain.model.queries.GetLatestTelemetrySnapshotQuery;
import com.andeva.atelier.platform.iot.domain.model.queries.GetTelemetrySnapshotHistoryQuery;
import com.andeva.atelier.platform.iot.domain.model.valueobjects.Obd2DeviceId;
import com.andeva.atelier.platform.iot.interfaces.rest.resources.IngestTelemetryBatchResource;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.TelemetryCommandFromResourceAssembler;
import com.andeva.atelier.platform.iot.interfaces.rest.transform.TelemetrySnapshotResourceFromAggregateAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    public VhTelemetryBatchesController(TelemetryCommandService commandService, TelemetryQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }


    @PostMapping
    @Operation(summary = "Ingest a batch of telemetry snapshots", description = "Stores a list of telemetry readings collected by an OBD2 device")
    public ResponseEntity<?> ingestTelemetryBatch(@Valid @RequestBody IngestTelemetryBatchResource resource) {
        var command = TelemetryCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = commandService.handle(command);

        return result.fold(
                snapshots -> {
                    var resources = snapshots.stream()
                            .map(TelemetrySnapshotResourceFromAggregateAssembler::toResourceFromAggregate)
                            .toList();
                    return ResponseEntity.status(HttpStatus.CREATED).body(resources);
                },
                failure -> {
                    if (failure instanceof TelemetryCommandFailure.NotFound) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(failure.message());
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(failure.message());
                    }
                }
        );
    }
}
