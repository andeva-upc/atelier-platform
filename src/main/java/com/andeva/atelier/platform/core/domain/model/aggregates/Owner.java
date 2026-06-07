package com.andeva.atelier.platform.core.domain.model.aggregates;

import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.OwnerId;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Phone;
import com.andeva.atelier.platform.core.domain.model.valueobjects.UserId;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

@Getter
public class Owner extends AbstractDomainAggregateRoot<Owner> {

    private OwnerId id;
    private UserId userId;
    private PersonName name;
    private Document document;
    private Phone phone;

    public Owner() {
    }

    public Owner(UserId userId, PersonName name, Document document, Phone phone) {
        this.userId = userId;
        this.name = name;
        this.document = document;
        this.phone = phone;
    }

    public Owner(OwnerId id, UserId userId, PersonName name, Document document, Phone phone) {
        this(userId, name, document, phone);
        this.id = id;
    }

    public void update(PersonName name, Document document, Phone phone) {
        this.name = name;
        this.document = document;
        this.phone = phone;
    }
}
