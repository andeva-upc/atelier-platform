package com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.converters;

import com.andeva.atelier.platform.shared.domain.model.valueobjects.Mileage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MileageAttributeConverter implements AttributeConverter<Mileage, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Mileage attribute) {
        return (attribute == null) ? null : attribute.value();
    }
    @Override
    public Mileage convertToEntityAttribute(Integer dbData) {
        return (dbData == null) ? null : new Mileage(dbData);
    }
}
