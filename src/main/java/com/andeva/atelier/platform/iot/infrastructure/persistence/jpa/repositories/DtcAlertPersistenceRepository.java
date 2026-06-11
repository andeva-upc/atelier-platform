package com.andeva.atelier.platform.iot.infrastructure.persistence.jpa.repositories;

import com.andeva.atelier.platform.iot.infrastructure.persistence.jpa.entities.DtcAlertPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA Repository for "dtc_alerts".
 */
@Repository
public interface DtcAlertPersistenceRepository extends JpaRepository<DtcAlertPersistenceEntity, UUID> {

    /**
     * Finds all DTC alerts generated under a specific OBD2 device registration.
     * @param registrationId the registration UUID
     * @return the list of DTC alerts ordered by creation date descending
     */
    @Query(value = "SELECT d.* FROM dtc_alerts d " +
                   "JOIN telemetry_snapshots t ON d.telemetry_snapshot_id = t.id " +
                   "WHERE t.obd2_device_registration_id = :registrationId " +
                   "ORDER BY d.created_at DESC", nativeQuery = true)
    List<DtcAlertPersistenceEntity> findAllByRegistrationId(@Param("registrationId") UUID registrationId);
}
