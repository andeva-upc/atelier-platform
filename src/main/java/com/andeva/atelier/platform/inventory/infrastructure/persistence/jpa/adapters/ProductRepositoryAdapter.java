package com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.adapters;
import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.Sku;
import com.andeva.atelier.platform.inventory.domain.repositories.ProductRepository;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.assemblers.ProductEntityAssembler;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.entities.ProductJpaEntity;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.repositories.ProductSpringDataRepository;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Component
public class ProductRepositoryAdapter implements ProductRepository {
    private final ProductSpringDataRepository springDataRepository;
    public ProductRepositoryAdapter(ProductSpringDataRepository springDataRepository) { this.springDataRepository = springDataRepository; }
    @Override public void save(Product product) { ProductJpaEntity entity = ProductEntityAssembler.toEntity(product); springDataRepository.save(entity); }
    @Override public Optional<Product> findById(UUID id) { return springDataRepository.findById(id).map(ProductEntityAssembler::toAggregate); }
    @Override public boolean existsBySkuAndBranchId(Sku sku, BranchId branchId) { return springDataRepository.existsBySkuAndBranchId(sku.value(), branchId.value()); }
    @Override public List<Product> findByBranchId(BranchId branchId) { return springDataRepository.findByBranchId(branchId.value()).stream().map(ProductEntityAssembler::toAggregate).collect(Collectors.toList()); }
}
