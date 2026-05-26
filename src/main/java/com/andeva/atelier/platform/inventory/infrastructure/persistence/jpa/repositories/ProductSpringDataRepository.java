package com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.repositories;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.entities.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
@Repository
public interface ProductSpringDataRepository extends JpaRepository<ProductJpaEntity, UUID> {
    boolean existsBySkuAndBranchId(String sku, String branchId);
    List<ProductJpaEntity> findByBranchId(String branchId);
}
