package com.andeva.atelier.platform.billing.interfaces.rest.transform;

import com.andeva.atelier.platform.billing.domain.model.commands.CreateQuoteCommand;
import com.andeva.atelier.platform.billing.interfaces.rest.resources.CreateQuoteResource;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;

public class CreateQuoteCommandFromResourceAssembler {
    public static CreateQuoteCommand toCommandFromResource(CreateQuoteResource resource) {
        return new CreateQuoteCommand(
                resource.workOrderId(),
                new BranchId(resource.branchId()),
                resource.discountPercentage()
        );
    }
}
