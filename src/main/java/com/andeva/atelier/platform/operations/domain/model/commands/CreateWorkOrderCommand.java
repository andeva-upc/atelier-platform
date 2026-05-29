package com.andeva.atelier.platform.operations.domain.model.commands;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.*;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.*;

public record CreateWorkOrderCommand(
        AppointmentId appointmentId,
        BranchId branchId,
        VehicleId vehicleId,
        CustomerId customerId,
        Integer internalNumber,
        DiagnosticSummary diagnosticSummary,
        Mileage mileageIn
) {}
