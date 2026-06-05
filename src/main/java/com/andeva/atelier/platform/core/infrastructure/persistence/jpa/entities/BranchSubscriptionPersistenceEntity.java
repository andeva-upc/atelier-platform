package com.andeva.atelier.platform.core.infrastructure.persistence.jpa.entities;

import com.andeva.atelier.platform.core.domain.model.valueobjects.BillingCycle;
import com.andeva.atelier.platform.core.domain.model.valueobjects.SubscriptionStatus;
import com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "branch_subscriptions")
@Getter
@Setter
@NoArgsConstructor
public class BranchSubscriptionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "branch_id", nullable = false)
    private UUID branchId;

    @Column(name = "plan_id", nullable = false)
    private UUID planId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_cycle", nullable = false)
    private BillingCycle billingCycle;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "canceled_at")
    private Date canceledAt;
}
