package com.andeva.atelier.platform.inventory.application.internal.commandservices;

import com.andeva.atelier.platform.inventory.application.commandservices.ProductCommandService;
import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.commands.CreateProductCommand;
import com.andeva.atelier.platform.inventory.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductCommandServiceImpl implements ProductCommandService {
    private final ProductRepository productRepository;

    public ProductCommandServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<Product> handle(CreateProductCommand command) {
        Product product = new Product(UUID.randomUUID(), command.branchId(), command.category(), command.name(), command.sku());
        productRepository.save(product);
        return Optional.of(product);
    }

    @Override
    public Optional<com.andeva.atelier.platform.inventory.domain.model.entities.ProductBatch> handle(com.andeva.atelier.platform.inventory.domain.model.commands.AddBatchToProductCommand command) {
        var product = productRepository.findById(command.productId());
        if (product.isEmpty()) {
            return Optional.empty();
        }
        var batch = new com.andeva.atelier.platform.inventory.domain.model.entities.ProductBatch(
                UUID.randomUUID(),
                command.quantity(),
                command.acquisitionCost()
        );
        product.get().addBatch(batch);
        productRepository.save(product.get());
        return Optional.of(batch);
    }

    @Override
    public Optional<Product> handle(com.andeva.atelier.platform.inventory.domain.model.commands.UpdateProductCommand command) {
        var product = productRepository.findById(command.productId());
        if (product.isEmpty()) {
            return Optional.empty();
        }
        product.get().updateDetails(command.name(), command.category(), command.sku());
        productRepository.save(product.get());
        return product;
    }
}
