package com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.converters;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.DiagnosticSummary;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DiagnosticSummaryAttributeConverter implements AttributeConverter<DiagnosticSummary, String> {
    @Override
    public String convertToDatabaseColumn(DiagnosticSummary attribute) {
        return (attribute == null) ? null : attribute.value();
    }
    @Override
    public DiagnosticSummary convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isBlank()) ? null : new DiagnosticSummary(dbData);
    }
}
