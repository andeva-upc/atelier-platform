package com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

/**
 * Implements with Spring Security
 */

@Configuration
public class AuditConfiguration {

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> {
            // Retorno de prueba por defecto (reemplazar por lógica real de Spring Security)
            return Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        };
    }
}