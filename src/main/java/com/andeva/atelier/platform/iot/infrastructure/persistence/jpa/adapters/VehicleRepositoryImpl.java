package com.andeva.atelier.platform.iot.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.iot.domain.model.aggregates.Vehicle;
import com.andeva.atelier.platform.iot.domain.repositories.VehicleRepository;
import com.andeva.atelier.platform.iot.infrastructure.persistence.jpa.assemblers.VehiclePersistenceAssembler;
import com.andeva.atelier.platform.iot.infrastructure.persistence.jpa.repositories.VehiclePersistenceRepository;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA adapter implementing the VehicleRepository port.
 */
@Repository
public class VehicleRepositoryImpl implements VehicleRepository {

    private final VehiclePersistenceRepository persistenceRepository;

    public VehicleRepositoryImpl(VehiclePersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public List<Vehicle> findAvailableForLinkingByBranchId(BranchId branchId) {
        return persistenceRepository.findAvailableForLinkingByBranchId(branchId.value()).stream()
                .map(VehiclePersistenceAssembler::toDomainEntity)
                .toList();
    }
}
