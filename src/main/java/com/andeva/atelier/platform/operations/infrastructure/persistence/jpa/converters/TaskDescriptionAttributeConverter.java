package com.andeva.atelier.platform.operations.infrastructure.persistence.jpa.converters;

import com.andeva.atelier.platform.operations.domain.model.valueobjects.TaskDescription;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TaskDescriptionAttributeConverter implements AttributeConverter<TaskDescription, String> {
    @Override
    public String convertToDatabaseColumn(TaskDescription attribute) {
        return (attribute == null) ? null : attribute.value();
    }
    @Override
    public TaskDescription convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isBlank()) ? null : new TaskDescription(dbData);
    }
}
