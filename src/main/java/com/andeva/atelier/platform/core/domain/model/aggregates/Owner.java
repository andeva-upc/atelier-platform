package com.andeva.atelier.platform.core.domain.model.aggregates;

import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Owner extends AbstractDomainAggregateRoot<Owner> {

    @Setter
    private UUID id;

    @Setter
    private UUID userId;

    @Setter
    private PersonName name;

    @Setter
    private Document document;

    @Setter
    private String phone;

    public Owner() {
    }

    public Owner(UUID userId, PersonName name, Document document, String phone) {
        if (userId == null) throw new IllegalArgumentException("core.error.userId.required");
        if (name == null) throw new IllegalArgumentException("core.error.personName.required");
        if (document == null) throw new IllegalArgumentException("core.error.document.required");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("core.error.phone.required");

        this.userId = userId;
        this.name = name;
        this.document = document;
        this.phone = phone;
    }

    public void update(String firstName, String lastName, String documentType, String documentNumber, String phone) {
        if (firstName == null || firstName.isBlank() || lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("core.error.personName.required");
        }
        this.name = new PersonName(firstName, lastName);
        this.document = new Document(documentType, documentNumber);
        this.phone = phone;
    }
}
