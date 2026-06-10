package com.andeva.atelier.platform.core.domain.model.aggregates;

import com.andeva.atelier.platform.core.domain.model.valueobjects.Phone;
import com.andeva.atelier.platform.core.domain.model.valueobjects.WorkshopId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Address;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import lombok.Getter;
import java.util.UUID;

@Getter
public class Branch extends AbstractDomainAggregateRoot<Branch> {

    private BranchId id;
    private WorkshopId workshopId;
    private String code;
    private String name;
    private Address address;
    private Phone phone;

    public Branch() {}

    public Branch(BranchId id, WorkshopId workshopId, String code, String name, Address address, Phone phone) {
        this.id = id;
        this.workshopId = workshopId;
        this.code = code;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Branch(WorkshopId workshopId, String code, String name, Address address, Phone phone) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("core.error.code.required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("core.error.name.required");
        this.id = new BranchId(UUID.randomUUID());
        this.workshopId = workshopId;
        this.code = code;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public void update(String code, String name, Address address, Phone phone) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("core.error.code.required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("core.error.name.required");
        
        this.code = code;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }
}

