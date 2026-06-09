package com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.repositories.ProductRepository;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.assemblers.ProductEntityAssembler;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.entities.ProductJpaEntity;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.repositories.ProductJpaRepository;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementing the domain ProductRepository interface.
 * Translates domain calls to JPA operations using the ProductEntityAssembler.
 * @author Adiel Sanchez
 */
@Component
public class ProductRepositoryAdapter implements ProductRepository {
    private final ProductJpaRepository jpaRepository;

    public ProductRepositoryAdapter(ProductJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Product product) {
        ProductJpaEntity entity = ProductEntityAssembler.toEntity(product);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id).map(ProductEntityAssembler::toAggregate);
    }

    @Override
    public List<Product> findAllByBranchId(BranchId branchId) {
        String branchIdStr = branchId.value().toString();
        return jpaRepository.findAllByBranchId(branchIdStr)
                .stream()
                .map(ProductEntityAssembler::toAggregate)
                .toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
