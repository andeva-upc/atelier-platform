package com.andeva.atelier.platform.core.domain.model.aggregates;

import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.EmployeeId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Phone;
import com.andeva.atelier.platform.core.domain.model.valueobjects.UserId;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import lombok.Getter;
import java.util.UUID;

@Getter
public class Employee extends AbstractDomainAggregateRoot<Employee> {

    private EmployeeId id;
    private UserId userId;
    private PersonName name;
    private Document document;
    private Phone phone;

    public Employee() {}

    public Employee(EmployeeId id, UserId userId, PersonName name, Document document, Phone phone) {
        this.id = id;
        this.userId = userId;
        this.document = document;
        this.phone = phone;
    }

    public Employee(UserId userId, PersonName name, Document document, Phone phone) {
        this.id = new EmployeeId(UUID.randomUUID());
        this.userId = userId;
        this.name = name;
        this.document = document;
        this.phone = phone;
    }

    public void update(PersonName name, Document document, Phone phone) {
        this.name = name;
        this.document = document;
        this.phone = phone;
    }
}
