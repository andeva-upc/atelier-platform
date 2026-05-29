package com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.converters;

import com.andeva.atelier.platform.shared.domain.model.valueobjects.Address;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AddressAttributeConverter implements AttributeConverter<Address, String> {
    @Override
    public String convertToDatabaseColumn(Address attribute) {
        return (attribute == null) ? null : attribute.value();
    }
    @Override
    public Address convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isBlank()) ? null : new Address(dbData);
    }
}