package com.andeva.atelier.platform.core.domain.model.aggregates;

import com.andeva.atelier.platform.shared.domain.model.valueobjects.CustomerId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Phone;
import com.andeva.atelier.platform.core.domain.model.valueobjects.UserId;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

@Getter
public class Customer extends AbstractDomainAggregateRoot<Customer> {

    private CustomerId id;
    private UserId userId;
    private boolean isCorporate;
    private PersonName name;
    private String businessName;
    private Document document;
    private Phone phone;

    public Customer() {
    }

    public Customer(UserId userId, boolean isCorporate, PersonName name, String businessName, Document document, Phone phone) {
        if (isCorporate && (businessName == null || businessName.isBlank())) {
            throw new IllegalArgumentException("core.error.businessName.required");
        }
        if (!isCorporate && name == null) {
            throw new IllegalArgumentException("core.error.personName.required");
        }

        this.userId = userId;
        this.isCorporate = isCorporate;
        this.name = name;
        this.businessName = businessName;
        this.document = document;
        this.phone = phone;
    }

    public Customer(CustomerId id, UserId userId, boolean isCorporate, PersonName name, String businessName, Document document, Phone phone) {
        this(userId, isCorporate, name, businessName, document, phone);
        this.id = id;
    }

    public void update(PersonName name, String businessName, Document document, Phone phone) {
        if (this.isCorporate) {
            if (!this.document.getDocumentType().name().equalsIgnoreCase(document.getDocumentType().name())) {
                throw new IllegalArgumentException("core.error.customer.corporateDocumentTypeImmutable");
            }
            if (businessName == null || businessName.isBlank()) {
                throw new IllegalArgumentException("core.error.businessName.required");
            }
            this.businessName = businessName;
        } else {
            if (name == null) {
                throw new IllegalArgumentException("core.error.personName.required");
            }
            this.name = name;
        }
        
        this.document = document;
        this.phone = phone;
    }
}

