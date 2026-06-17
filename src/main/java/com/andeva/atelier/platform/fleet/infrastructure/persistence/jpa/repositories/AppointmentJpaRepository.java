package com.andeva.atelier.platform.fleet.infrastructure.persistence.jpa.repositories;

import com.andeva.atelier.platform.fleet.domain.model.valueobjects.AppointmentStatus;
import com.andeva.atelier.platform.fleet.infrastructure.persistence.jpa.entities.AppointmentPersistenceEntity;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentJpaRepository extends JpaRepository<AppointmentPersistenceEntity, UUID> {

        boolean existsByScheduledStartLessThanAndScheduledEndGreaterThan(
                        LocalDateTime scheduledEnd, LocalDateTime scheduledStart);

        boolean existsByIdNotAndScheduledStartLessThanAndScheduledEndGreaterThan(
                        UUID appointmentId, LocalDateTime scheduledEnd, LocalDateTime scheduledStart);

        List<AppointmentPersistenceEntity> findByBranchId(BranchId branchId);

        List<AppointmentPersistenceEntity> findByCustomerId(UUID customerId);

        List<AppointmentPersistenceEntity> findByBranchIdAndStatus(
                        BranchId branchId, AppointmentStatus status);
}