package com.andeva.atelier.platform.core.domain.model.aggregates;

import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Customer extends AbstractDomainAggregateRoot<Customer> {

    @Setter
    private UUID id;

    @Setter
    private UUID userId;

    @Setter
    private boolean isCorporate;

    @Setter
    private PersonName name;

    @Setter
    private String businessName;

    @Setter
    private Document document;

    @Setter
    private String phone;

    public Customer() {
    }

    public Customer(UUID userId, boolean isCorporate, PersonName name, String businessName, Document document, String phone) {
        if (userId == null) throw new IllegalArgumentException("core.error.userId.required");
        if (document == null) throw new IllegalArgumentException("core.error.document.required");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("core.error.phone.required");

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

    public void update(String firstName, String lastName, String businessName, String documentType, String documentNumber, String phone) {
        if (this.isCorporate) {
            if (!this.document.getDocumentType().name().equalsIgnoreCase(documentType)) {
                throw new IllegalArgumentException("Corporate customers cannot change their document type.");
            }
            if (businessName == null || businessName.isBlank()) {
                throw new IllegalArgumentException("core.error.businessName.required");
            }
            this.businessName = businessName;
        } else {
            if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
                throw new IllegalArgumentException("core.error.personName.required");
            }
            this.name = new PersonName(firstName, lastName);
        }
        
        this.document = new Document(documentType, documentNumber);
        this.phone = phone;
    }
}
