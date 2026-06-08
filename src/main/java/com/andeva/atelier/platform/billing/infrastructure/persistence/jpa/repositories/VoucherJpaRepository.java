package com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.repositories;

import com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.entities.VoucherPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VoucherJpaRepository extends JpaRepository<VoucherPersistenceEntity, UUID> {
}
