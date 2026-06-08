package com.andeva.atelier.platform.inventory.domain.repositories;

import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    void save(Product product);
    Optional<Product> findById(UUID id);
}
