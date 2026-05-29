package com.andeva.atelier.platform.operations.domain.model.events;

import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrderTaskProduct;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import java.util.List;

public record WorkOrderPaidEvent(
        Object source,
        BranchId branchId,
        List<WorkOrderTaskProduct> dispatchedProducts
) {}
