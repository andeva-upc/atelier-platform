package com.andeva.atelier.platform.inventory.application.internal.eventhandlers;
import com.andeva.atelier.platform.operations.domain.model.events.ProductReservedEvent;
import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.repositories.ProductRepository;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.InventoryQuantity;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;
@Component
public class InventoryStockListener {
    private final ProductRepository productRepository;
    public InventoryStockListener(ProductRepository productRepository) { this.productRepository = productRepository; }
    @EventListener
    public void on(ProductReservedEvent event) {
        Optional<Product> productOpt = productRepository.findById(UUID.fromString(event.productId()));
        productOpt.ifPresent(product -> {
            product.reserveStock(new InventoryQuantity(event.quantity()));
            productRepository.save(product);
        });
    }
}
