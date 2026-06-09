package com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Voucher;
import com.andeva.atelier.platform.billing.domain.repositories.VoucherRepository;
import com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.assemblers.VoucherPersistenceAssembler;
import com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.entities.VoucherPersistenceEntity;
import com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.repositories.VoucherJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;

@Component
public class VoucherRepositoryImpl implements VoucherRepository {

    private final VoucherJpaRepository persistenceRepository;

    public VoucherRepositoryImpl(VoucherJpaRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Voucher save(Voucher voucher) {
        boolean exists = voucher.getId() != null && persistenceRepository.existsById(voucher.getId());
        
        VoucherPersistenceEntity entity;
        if (exists) {
            entity = persistenceRepository.findById(voucher.getId()).orElseThrow();
        } else {
            entity = new VoucherPersistenceEntity();
        }
        
        VoucherPersistenceAssembler.updateEntityFromAggregate(entity, voucher);
        
        var savedEntity = persistenceRepository.save(entity);
        return VoucherPersistenceAssembler.toAggregate(savedEntity);
    }

    @Override
    public Optional<Voucher> findById(UUID id) {
        return persistenceRepository.findById(id)
                .map(VoucherPersistenceAssembler::toAggregate);
    }

    @Override
    public List<Voucher> findByBranchId(BranchId branchId) {
        return persistenceRepository.findByBranchId(branchId.id())
                .stream()
                .map(VoucherPersistenceAssembler::toAggregate)
                .toList();
    }
}
