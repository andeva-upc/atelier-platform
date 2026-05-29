package com.andeva.atelier.platform.operations.interfaces.rest.transform;

import com.andeva.atelier.platform.operations.application.commandservices.WorkOrderCommandFailure;
import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrder;
import com.andeva.atelier.platform.shared.application.result.Result;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Locale;

/**
 * Assembler class to map command execution Results into proper HTTP ResponseEntity payloads.
 */
public final class ResponseEntityFromWorkOrderCommandResultAssembler {

    private ResponseEntityFromWorkOrderCommandResultAssembler() {}

    /**
     * Convierte el Result de un comando a una ResponseEntity lista para retornar.
     */
    public static ResponseEntity<?> toResponseEntityFromResult(
            Result<WorkOrder, WorkOrderCommandFailure> result,
            MessageSource messageSource) {

        return result.fold(
                // Caso Exitoso (201 Created o 200 OK)
                workOrder -> new ResponseEntity<>(
                        WorkOrderResourceFromAggregateAssembler.toResourceFromAggregate(workOrder),
                        HttpStatus.OK
                ),
                // Caso Fallido (Mapeamos el error de negocio al estado HTTP correcto)
                failure -> {
                    HttpStatus status = statusFromFailure(failure);
                    String localizedMessage = messageFromFailure(failure, messageSource);
                    return ResponseEntity.status(status).body(
                            ProblemDetail.forStatusAndDetail(status, localizedMessage)
                    );
                }
        );
    }

    private static HttpStatus statusFromFailure(WorkOrderCommandFailure failure) {
        return switch (failure) {
            case WorkOrderCommandFailure.Duplicate(String _) -> HttpStatus.CONFLICT;
            case WorkOrderCommandFailure.NotFound(String _) -> HttpStatus.NOT_FOUND;
            case WorkOrderCommandFailure.InvalidState(String _) -> HttpStatus.BAD_REQUEST;
        };
    }

    private static String messageFromFailure(WorkOrderCommandFailure failure, MessageSource messageSource) {
        // Expresión switch exhaustiva con deconstrucción de registros (Record Patterns)
        String messageKey = switch (failure) {
            case WorkOrderCommandFailure.Duplicate(String message) -> message;
            case WorkOrderCommandFailure.NotFound(String message) -> message;
            case WorkOrderCommandFailure.InvalidState(String message) -> message;
        };
        try {
            return messageSource.getMessage(messageKey, null, Locale.getDefault());
        } catch (Exception e) {
            return messageKey;
        }
    }
}