package com.andeva.atelier.platform.core.domain.model.aggregates;

import com.andeva.atelier.platform.core.domain.model.valueobjects.OwnerId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.WorkshopId;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

@Getter
public class Workshop extends AbstractDomainAggregateRoot<Workshop> {

    private WorkshopId id;
    private OwnerId ownerId;
    private String businessName;
    private String brandName;
    private String taxId; // RUC
    private int mileageIntervalConfig;

    public Workshop() {
        this.mileageIntervalConfig = 1;
    }

    public Workshop(OwnerId ownerId, String businessName, String brandName, String taxId, int mileageIntervalConfig) {
        if (businessName == null || businessName.isBlank()) throw new IllegalArgumentException("core.error.businessName.required");
        if (brandName == null || brandName.isBlank()) throw new IllegalArgumentException("core.error.brandName.required");
        if (taxId == null || taxId.isBlank()) throw new IllegalArgumentException("core.error.taxId.required");

        this.ownerId = ownerId;
        this.businessName = businessName;
        this.brandName = brandName;
        this.taxId = taxId;
        this.mileageIntervalConfig = mileageIntervalConfig;
    }

    public Workshop(WorkshopId id, OwnerId ownerId, String businessName, String brandName, String taxId, int mileageIntervalConfig) {
        this(ownerId, businessName, brandName, taxId, mileageIntervalConfig);
        this.id = id;
    }

    public void update(String businessName, String brandName, String taxId, int mileageIntervalConfig) {
        if (businessName == null || businessName.isBlank()) throw new IllegalArgumentException("core.error.businessName.required");
        if (brandName == null || brandName.isBlank()) throw new IllegalArgumentException("core.error.brandName.required");
        if (taxId == null || taxId.isBlank()) throw new IllegalArgumentException("core.error.taxId.required");

        this.businessName = businessName;
        this.brandName = brandName;
        this.taxId = taxId;
        this.mileageIntervalConfig = mileageIntervalConfig;
    }
}
