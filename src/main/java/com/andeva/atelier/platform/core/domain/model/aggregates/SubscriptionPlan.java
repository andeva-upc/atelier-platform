package com.andeva.atelier.platform.core.domain.model.aggregates;

import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class SubscriptionPlan extends AbstractDomainAggregateRoot<SubscriptionPlan> {

    @Setter
    private UUID id;

    @Setter
    private String name;

    @Setter
    private double monthlyPrice;

    @Setter
    private int maxObd2Devices;

    @Setter
    private int maxMonthlySnapshotsPerVehicle;

    @Setter
    private int maxCustomers;

    @Setter
    private int maxStaffAccounts;

    @Setter
    private boolean isActive;

    public SubscriptionPlan() {
        this.isActive = true;
    }
}
