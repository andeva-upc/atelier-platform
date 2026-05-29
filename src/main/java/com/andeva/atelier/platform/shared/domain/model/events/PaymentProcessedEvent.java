package com.andeva.atelier.platform.shared.domain.model.events;

import java.util.UUID;
/**
 * Evento de integración compartido que se dispara desde el contexto de Billing
 * cuando un pago es procesado con éxito para una Orden de Trabajo.
 */
public record PaymentProcessedEvent(UUID workOrderId) {}
