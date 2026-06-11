package com.andeva.atelier.platform.iot.application.internal.commandservices;

import com.andeva.atelier.platform.iot.application.commandservices.VehicleCommandFailure;
import com.andeva.atelier.platform.iot.application.commandservices.VehicleCommandService;
import com.andeva.atelier.platform.iot.domain.model.aggregates.Vehicle;
import com.andeva.atelier.platform.iot.domain.model.aggregates.VehicleRegistration;
import com.andeva.atelier.platform.iot.domain.model.commands.RegisterVehicleCommand;
import com.andeva.atelier.platform.iot.domain.repositories.VehicleRegistrationRepository;
import com.andeva.atelier.platform.iot.domain.repositories.VehicleRepository;
import com.andeva.atelier.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service implementation for handling Vehicle command operations.
 */
@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;
    private final VehicleRegistrationRepository vehicleRegistrationRepository;

    public VehicleCommandServiceImpl(
            VehicleRepository vehicleRepository,
            VehicleRegistrationRepository vehicleRegistrationRepository
    ) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleRegistrationRepository = vehicleRegistrationRepository;
    }

    @Override
    @Transactional
    public Result<VehicleRegistration, VehicleCommandFailure> handle(RegisterVehicleCommand command) {
        // 1. Check if vehicle already exists by VIN or Plate Number
        Optional<Vehicle> vehicleByVin = vehicleRepository.findByVin(command.vin());
        Optional<Vehicle> vehicleByPlate = vehicleRepository.findByPlateNumber(command.plateNumber());

        Vehicle vehicle;
        if (vehicleByVin.isPresent() && vehicleByPlate.isPresent()) {
            // Both queries returned a vehicle. Ensure they point to the exact same vehicle.
            if (!vehicleByVin.get().getId().equals(vehicleByPlate.get().getId())) {
                return Result.failure(new VehicleCommandFailure.Duplicate("iot.error.vehicle.conflict"));
            }
            vehicle = vehicleByVin.get();
        } else if (vehicleByVin.isPresent()) {
            vehicle = vehicleByVin.get();
        } else if (vehicleByPlate.isPresent()) {
            vehicle = vehicleByPlate.get();
        } else {
            // 2. If vehicle doesn't exist, create it
            vehicle = new Vehicle(
                    command.plateNumber(),
                    command.brand(),
                    command.model(),
                    command.year(),
                    command.vin()
            );
            vehicle = vehicleRepository.save(vehicle);
        }

        // 3. Check if there is an active registration for this vehicle
        Optional<VehicleRegistration> activeRegistrationOpt = vehicleRegistrationRepository.findActiveByVehicleId(vehicle.getId());
        if (activeRegistrationOpt.isPresent()) {
            VehicleRegistration activeRegistration = activeRegistrationOpt.get();
            if (activeRegistration.getUserId().equals(command.userId())) {
                // If it is already registered by the same user, just return the active registration
                return Result.success(activeRegistration);
            } else {
                // If registered to another user, deactivate/mark as PREVIOUS
                activeRegistration.deactivateRegistration();
                vehicleRegistrationRepository.save(activeRegistration);
            }
        }

        // 4. Create and persist new active VehicleRegistration coupling the vehicle to the registering user
        VehicleRegistration newRegistration = new VehicleRegistration(command.userId(), vehicle.getId());
        VehicleRegistration savedRegistration = vehicleRegistrationRepository.save(newRegistration);

        return Result.success(savedRegistration);
    }
}
