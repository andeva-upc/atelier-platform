package com.andeva.atelier.platform.inventory.application.internal.eventhandlers;

import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.InventoryQuantity;
import com.andeva.atelier.platform.inventory.domain.repositories.ProductRepository;
import com.andeva.atelier.platform.operations.domain.model.aggregates.WorkOrderTaskProduct;
import com.andeva.atelier.platform.operations.domain.model.events.ProductReservationCanceledEvent;
import com.andeva.atelier.platform.operations.domain.model.events.ProductReservedEvent;
import com.andeva.atelier.platform.operations.domain.model.events.WorkOrderPaidEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InventoryStockListener {
    private final ProductRepository productRepository;

    public InventoryStockListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventListener
    public void on(ProductReservedEvent event) {
        Optional<Product> productOpt = productRepository.findById(event.productId().value());
        productOpt.ifPresent(product -> {
            product.reserveStock(new InventoryQuantity(event.quantity().value()));
            productRepository.save(product);
        });
    }

    @EventListener
    public void on(ProductReservationCanceledEvent event) {
        Optional<Product> productOpt = productRepository.findById(event.productId().value());
        productOpt.ifPresent(product -> {
            product.releaseStock(new InventoryQuantity(event.quantity().value()));
            productRepository.save(product);
        });
    }

    @EventListener
    public void on(WorkOrderPaidEvent event) {
        for (WorkOrderTaskProduct dispatchedProduct : event.dispatchedProducts()) {
            Optional<Product> productOpt = productRepository.findById(dispatchedProduct.getProductId().value());
            productOpt.ifPresent(product -> {
                product.dispatchStock(new InventoryQuantity(dispatchedProduct.getQuantity().value()));
                productRepository.save(product);
            });
        }
    }
}
