package com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

/**
 * Configuration class for setting up JPA auditing in the application.
 * This class defines a bean for providing the current auditor (user) information.
 * The auditor information is typically used to automatically populate auditing fields such as createdBy and lastModifiedBy in JPA entities.
 * In this example, the auditor is represented as a UUID, and a default value is returned for demonstration purposes. In a real application, you would replace this with logic to retrieve the actual user information from the security context or authentication mechanism in use (e.g., Spring Security).
 * @author Joel Huamani Estefanero
 */
@Configuration
public class AuditConfiguration {

    /**
     * Defines a bean that provides the current auditor information for JPA auditing. The auditor is represented as a UUID, and a default value is returned for demonstration purposes. In a real application, you would replace this with logic to retrieve the actual user information from the security context or authentication mechanism in use (e.g., Spring Security).
     * @return An instance of AuditorAware that provides the current auditor information as a UUID.
     */
    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> {
            // Retorno de prueba por defecto (reemplazar por lógica real de Spring Security)
            return Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        };
    }
}