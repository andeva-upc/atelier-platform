package com.andeva.atelier.platform.appointments.infrastructure.persistence.jpa.repositories;

import com.andeva.atelier.platform.appointments.infrastructure.persistence.jpa.entities.AppointmentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentPersistenceEntity, UUID> {
}