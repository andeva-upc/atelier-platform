package com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.repositories.ProductRepository;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.assemblers.ProductEntityAssembler;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.entities.ProductJpaEntity;
import com.andeva.atelier.platform.inventory.infrastructure.persistence.jpa.repositories.ProductJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

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
}
