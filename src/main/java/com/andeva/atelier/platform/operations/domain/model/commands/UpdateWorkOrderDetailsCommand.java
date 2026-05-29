package com.andeva.atelier.platform.operations.domain.model.commands;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.DiagnosticSummary;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Mileage;

import java.util.UUID;
public record UpdateWorkOrderDetailsCommand(
        UUID workOrderId,
        DiagnosticSummary diagnosticSummary,
        Mileage mileageIn
) {}
