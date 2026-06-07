package com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.repositories;

import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.entities.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID> {
}
