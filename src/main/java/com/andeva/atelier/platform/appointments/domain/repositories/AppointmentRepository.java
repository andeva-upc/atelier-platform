package com.andeva.atelier.platform.appointments.domain.repositories;

import com.andeva.atelier.platform.appointments.domain.model.aggregates.Appointment;

public interface AppointmentRepository {
    Appointment save(Appointment appointment);
}