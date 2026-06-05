package com.andeva.atelier.platform.core.domain.model.aggregates;

import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Address;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Branch extends AbstractDomainAggregateRoot<Branch> {

    @Setter
    private UUID id;

    @Setter
    private UUID workshopId;

    @Setter
    private String code;

    @Setter
    private String name;

    @Setter
    private Address address;

    @Setter
    private String phone;

    public Branch() {
    }

    public Branch(UUID workshopId, String code, String name, String address, String phone) {
        if (workshopId == null) throw new IllegalArgumentException("core.error.workshopId.required");
        if (code == null || code.isBlank()) throw new IllegalArgumentException("core.error.code.required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("core.error.name.required");

        this.workshopId = workshopId;
        this.code = code;
        this.name = name;
        this.address = new Address(address);
        this.phone = phone;
    }

    public void update(String code, String name, String address, String phone) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("core.error.code.required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("core.error.name.required");
        
        this.code = code;
        this.name = name;
        this.address = new Address(address);
        this.phone = phone;
    }
}
