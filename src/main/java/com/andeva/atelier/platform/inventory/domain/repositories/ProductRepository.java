package com.andeva.atelier.platform.inventory.domain.repositories;
import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.Sku;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(UUID id);
    boolean existsBySkuAndBranchId(Sku sku, BranchId branchId);
    List<Product> findByBranchId(BranchId branchId);
}
