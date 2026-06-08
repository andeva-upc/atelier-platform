package com.andeva.atelier.platform.appointments.domain.model.aggregates;

import com.andeva.atelier.platform.appointments.domain.model.commands.CreateAppointmentCommand;
import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Appointment extends AbstractDomainAggregateRoot<Appointment> {

    private UUID workshopId;
    private UUID branchId;
    private UUID customerId;
    private UUID vehicleId;
    private LocalDateTime appointmentDate;
    private String status;

    public Appointment(UUID workshopId, UUID branchId, UUID customerId, UUID vehicleId,
                       LocalDateTime appointmentDate, String status) {
        this.workshopId = workshopId;
        this.branchId = branchId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.appointmentDate = appointmentDate;
        this.status = status;
    }
}