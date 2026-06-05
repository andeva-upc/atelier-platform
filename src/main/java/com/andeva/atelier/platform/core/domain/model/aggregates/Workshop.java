package com.andeva.atelier.platform.core.domain.model.aggregates;

import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Workshop extends AbstractDomainAggregateRoot<Workshop> {

    @Setter
    private UUID id;

    @Setter
    private UUID ownerId;

    @Setter
    private String businessName;

    @Setter
    private String brandName;

    @Setter
    private String taxId; // RUC

    @Setter
    private int mileageIntervalConfig;

    public Workshop() {
        this.mileageIntervalConfig = 1;
    }

    public Workshop(UUID ownerId, String businessName, String brandName, String taxId, int mileageIntervalConfig) {
        if (ownerId == null) throw new IllegalArgumentException("core.error.ownerId.required");
        if (businessName == null || businessName.isBlank()) throw new IllegalArgumentException("core.error.businessName.required");
        if (brandName == null || brandName.isBlank()) throw new IllegalArgumentException("core.error.brandName.required");
        if (taxId == null || taxId.isBlank()) throw new IllegalArgumentException("core.error.taxId.required");

        this.ownerId = ownerId;
        this.businessName = businessName;
        this.brandName = brandName;
        this.taxId = taxId;
        this.mileageIntervalConfig = mileageIntervalConfig;
    }
}
