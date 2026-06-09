package com.andeva.atelier.platform.fleet.infrastructure.persistence.jpa.converters;

import com.andeva.atelier.platform.fleet.domain.model.valueobjects.AppointmentsSummary;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Attribute Converter for DiagnosticSummary value object. This converter handles the conversion between the DiagnosticSummary type and its String representation for database storage. It ensures that when a DiagnosticSummary is persisted, its value is stored as a String, and when retrieved, it is converted back to a DiagnosticSummary instance.
 * The converter is set to autoApply, meaning it will be automatically applied to all attributes of type DiagnosticSummary in the JPA entities without needing to specify it explicitly on each attribute.
 * @author Joel Huamani Estefanero
 */
@Converter(autoApply = true)
public class AppointmentsSummaryAttributeConverter implements AttributeConverter<AppointmentsSummary, String> {

    /**
     * Converts a AppointmentsSummary entity attribute to its String representation for database storage. If the attribute is null, it returns null; otherwise, it returns the value of the AppointmentsSummary.
     * @param attribute  the entity attribute value to be converted
     * @return the String representation of the AppointmentsSummary for database storage, or null if the attribute is null
     */
    @Override
    public String convertToDatabaseColumn(AppointmentsSummary attribute) {
        return (attribute == null) ? null : attribute.value();
    }

    /**
     * Converts a String from the database column back to a AppointmentsSummary entity attribute. If the database value is null or blank, it returns null; otherwise, it creates a new AppointmentsSummary instance using the String value.
     * @param dbData  the data from the database column to be converted
     * @return a AppointmentsSummary instance created from the database String value, or null if the database value is null or blank
     */
    @Override
    public AppointmentsSummary convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isBlank()) ? null : new AppointmentsSummary(dbData);
    }
}
