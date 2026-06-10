package com.andeva.atelier.platform.iot.application.internal.commandservices;

import com.andeva.atelier.platform.iot.application.commandservices.Obd2DeviceRegistrationCommandFailure;
import com.andeva.atelier.platform.iot.application.commandservices.Obd2DeviceRegistrationCommandService;
import com.andeva.atelier.platform.iot.domain.model.aggregates.Obd2Device;
import com.andeva.atelier.platform.iot.domain.model.aggregates.Obd2DeviceRegistration;
import com.andeva.atelier.platform.iot.domain.model.commands.LinkObd2DeviceToVehicleCommand;
import com.andeva.atelier.platform.iot.domain.model.valueobjects.Obd2DeviceId;
import com.andeva.atelier.platform.iot.domain.model.valueobjects.Obd2DeviceStatus;
import com.andeva.atelier.platform.iot.domain.repositories.Obd2DeviceRegistrationRepository;
import com.andeva.atelier.platform.iot.domain.repositories.Obd2DeviceRepository;
import com.andeva.atelier.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for handling OBD2 Device registration/linking commands.
 */
@Service
public class Obd2DeviceRegistrationCommandServiceImpl implements Obd2DeviceRegistrationCommandService {

    private final Obd2DeviceRepository obd2DeviceRepository;
    private final Obd2DeviceRegistrationRepository obd2DeviceRegistrationRepository;

    public Obd2DeviceRegistrationCommandServiceImpl(
            Obd2DeviceRepository obd2DeviceRepository,
            Obd2DeviceRegistrationRepository obd2DeviceRegistrationRepository
    ) {
        this.obd2DeviceRepository = obd2DeviceRepository;
        this.obd2DeviceRegistrationRepository = obd2DeviceRegistrationRepository;
    }

    @Override
    @Transactional
    public Result<Obd2DeviceRegistration, Obd2DeviceRegistrationCommandFailure> handle(LinkObd2DeviceToVehicleCommand command) {
        // 1. Validar existencia del OBD2
        var obd2DeviceOpt = obd2DeviceRepository.findById(command.obd2DeviceId());
        if (obd2DeviceOpt.isEmpty()) {
            return Result.failure(new Obd2DeviceRegistrationCommandFailure.NotFound("iot.error.obd2Device.notFound"));
        }

        var obd2Device = obd2DeviceOpt.get();

        // 2. Validar que el OBD2 no esté ya marcado como LINKED en su agregado
        if (Obd2DeviceStatus.LINKED.equals(obd2Device.getStatus())) {
            return Result.failure(new Obd2DeviceRegistrationCommandFailure.InvalidState("iot.error.obd2DeviceRegistration.deviceAlreadyLinked"));
        }

        // 3. Validar que el OBD2 no tenga ya una vinculación activa en base de datos (invariante de negocio)
        var activeDeviceRegOpt = obd2DeviceRegistrationRepository.findActiveByObd2DeviceId(command.obd2DeviceId());
        if (activeDeviceRegOpt.isPresent()) {
            return Result.failure(new Obd2DeviceRegistrationCommandFailure.InvalidState("iot.error.obd2DeviceRegistration.deviceAlreadyLinked"));
        }

        // 4. Validar que el vehículo no tenga una vinculación activa en base de datos (invariante de negocio)
        var activeVehicleRegOpt = obd2DeviceRegistrationRepository.findActiveByVehicleId(command.vehicleId());
        if (activeVehicleRegOpt.isPresent()) {
            return Result.failure(new Obd2DeviceRegistrationCommandFailure.InvalidState("iot.error.obd2DeviceRegistration.vehicleAlreadyLinked"));
        }

        // 5. Actualizar el estado del OBD2 a LINKED y guardar
        obd2Device.markAsLinked();
        obd2DeviceRepository.save(obd2Device);

        // 6. Crear la vinculación con estado ACTIVE (por defecto en el constructor de Obd2DeviceRegistration)
        var registration = new Obd2DeviceRegistration(
                command.obd2DeviceId(),
                command.branchId(),
                command.vehicleId()
        );

        // 7. Guardar en base de datos
        var savedRegistration = obd2DeviceRegistrationRepository.save(registration);

        return Result.success(savedRegistration);
    }
}
