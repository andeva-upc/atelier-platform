package com.andeva.atelier.platform.fleet.domain.model.aggregates;

import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.CustomerId;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class CustomerRegistration extends AbstractDomainAggregateRoot<CustomerRegistration> {

    private CustomerId id;
    private UUID customerId;
    private BranchId branchId;
    private String status;
    private Instant createdAt;
    private Instant deletedAt;

    public CustomerRegistration() {
    }

    public CustomerRegistration(UUID customerId, BranchId branchId) {
        this.id = new CustomerId(UUID.randomUUID());
        this.customerId = customerId;
        this.branchId = branchId;
        this.status = "ACTIVE";
        this.createdAt = Instant.now();
    }

    public CustomerRegistration(CustomerId id, UUID customerId, BranchId branchId, String status, Instant createdAt, Instant deletedAt) {
        this.id = id;
        this.customerId = customerId;
        this.branchId = branchId;
        this.status = status;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public void deactivate() {
        this.status = "INACTIVE";
        this.deletedAt = Instant.now();
    }
}

