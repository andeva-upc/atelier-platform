package com.andeva.atelier.platform.core.domain.model.entities;

import com.andeva.atelier.platform.core.domain.model.valueobjects.BillingCycle;
import com.andeva.atelier.platform.core.domain.model.valueobjects.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
public class BranchSubscription {

    @Setter
    private UUID id;

    @Setter
    private UUID branchId;

    @Setter
    private UUID planId;

    @Setter
    private SubscriptionStatus status;

    @Setter
    private BillingCycle billingCycle;

    @Setter
    private Date startDate;

    @Setter
    private Date endDate;

    @Setter
    private Date canceledAt;

    public BranchSubscription() {
    }

    public BranchSubscription(UUID branchId, UUID planId, BillingCycle billingCycle, Date startDate, Date endDate) {
        if (branchId == null) throw new IllegalArgumentException("core.error.branchId.required");
        if (planId == null) throw new IllegalArgumentException("core.error.planId.required");
        if (billingCycle == null) throw new IllegalArgumentException("core.error.billingCycle.required");
        if (startDate == null) throw new IllegalArgumentException("core.error.startDate.required");
        if (endDate == null) throw new IllegalArgumentException("core.error.endDate.required");

        this.branchId = branchId;
        this.planId = planId;
        this.status = SubscriptionStatus.ACTIVE;
        this.billingCycle = billingCycle;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void cancel(Date canceledAt) {
        this.status = SubscriptionStatus.CANCELED;
        this.canceledAt = canceledAt;
    }
}
