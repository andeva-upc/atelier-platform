package com.andeva.atelier.platform.operations.interfaces.events;

import com.andeva.atelier.platform.operations.application.commandservices.WorkOrderCommandService;
import com.andeva.atelier.platform.operations.domain.model.commands.MarkWorkOrderAsPaidCommand;
import com.andeva.atelier.platform.shared.domain.model.events.PaymentProcessedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WorkOrderPaymentListener {
    private final WorkOrderCommandService commandService;
    // Inyección de dependencias por constructor
    public WorkOrderPaymentListener(WorkOrderCommandService commandService) {
        this.commandService = commandService;
    }
    /**
     * Escucha el evento PaymentProcessedEvent lanzado por el módulo de Billing.
     */
    @EventListener
    public void onPaymentProcessed(PaymentProcessedEvent event) {
        // Ejecuta de forma automática el caso de uso para marcar como pagado
        commandService.handle(new MarkWorkOrderAsPaidCommand(event.workOrderId()));
    }
}