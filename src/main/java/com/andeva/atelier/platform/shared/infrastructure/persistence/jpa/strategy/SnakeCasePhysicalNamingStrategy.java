package com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import static io.github.encryptorcode.pluralize.Pluralize.pluralize;

/**
 * A custom implementation of Hibernate's PhysicalNamingStrategy that converts entity and column names to snake_case and pluralizes table names.
 * This strategy ensures that the database schema follows a consistent naming convention, making it easier to read and maintain.
 */
public class SnakeCasePhysicalNamingStrategy implements PhysicalNamingStrategy {

    /**
     * Converts the given identifier to snake_case format. This method is used for catalog names, schema names, sequence names, and column names.
     * @param identifier the original identifier to be converted
     * @param jdbcEnvironment the JDBC environment, which can be used to access database metadata (not used in this implementation)
     * @return the converted identifier in snake_case format
     */
    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.toSnakeCase(identifier);
    }

    /**
     * Converts the given identifier to snake_case format. This method is used for schema names. It calls the toSnakeCase method to perform the conversion.
     * @param identifier the original identifier to be converted
     * @param jdbcEnvironment the JDBC environment, which can be used to access database metadata (not used in this implementation)
     * @return the converted identifier in snake_case format
     */
    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.toSnakeCase(identifier);
    }

    /**
     * Converts the given identifier to snake_case format and pluralizes it. This method is used for table names. It first calls the toPlural method to pluralize the name, and then calls the toSnakeCase method to convert it to snake_case format.
     * @param identifier the original identifier to be converted
     * @param jdbcEnvironment the JDBC environment, which can be used to access database metadata (not used in this implementation)
     * @return the converted identifier in snake_case format and pluralized
     */
    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.toSnakeCase(this.toPlural(identifier));
    }

    /**
     * Converts the given identifier to snake_case format. This method is used for sequence names. It calls the toSnakeCase method to perform the conversion.
     * @param identifier the original identifier to be converted
     * @param jdbcEnvironment the JDBC environment, which can be used to access database metadata (not used in this implementation)
     * @return the converted identifier in snake_case format
     */
    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.toSnakeCase(identifier);
    }

    /**
     * Converts the given identifier to snake_case format. This method is used for column names. It calls the toSnakeCase method to perform the conversion.
     * @param identifier the original identifier to be converted
     * @param jdbcEnvironment the JDBC environment, which can be used to access database metadata (not used in this implementation)
     * @return the converted identifier in snake_case format
     */
    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.toSnakeCase(identifier);
    }

    /**
     * Helper method that converts a given identifier to snake_case format. It uses a regular expression to identify the transition from lowercase to uppercase letters and inserts an underscore between them, then converts the entire string to lowercase.
     * @param identifier the original identifier to be converted
     * @return the converted identifier in snake_case format, or null if the input identifier is null
     */
    private Identifier toSnakeCase(final Identifier identifier) {
        if (identifier == null) return null;

        final String regex = "([a-z])([A-Z])";
        final String replacement = "$1_$2";
        final String newName = identifier.getText()
                .replaceAll(regex, replacement)
                .toLowerCase();
        return Identifier.toIdentifier(newName);
    }

    /**
     * Helper method that pluralizes a given identifier. It uses the Pluralize library to convert the singular form of the identifier to its plural form.
     * @param identifier the original identifier to be pluralized
     * @return the pluralized identifier, or null if the input identifier is null
     */
    private Identifier toPlural(final Identifier identifier) {
        final String newName = pluralize(identifier.getText());
        return Identifier.toIdentifier(newName);
    }
}
