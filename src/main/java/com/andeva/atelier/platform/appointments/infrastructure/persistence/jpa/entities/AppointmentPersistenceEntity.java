package com.andeva.atelier.platform.appointments.infrastructure.persistence.jpa.entities;

import com.andeva.atelier.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@lombok.NoArgsConstructor
public class AppointmentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID workshopId;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID branchId;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID customerId;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID vehicleId;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Column(nullable = false, length = 20)
    private String status;
}