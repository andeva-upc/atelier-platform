package com.andeva.atelier.platform.inventory.interfaces.rest;

import com.andeva.atelier.platform.inventory.application.commandservices.ProductCommandService;
import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.commands.CreateProductCommand;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.*;
import com.andeva.atelier.platform.inventory.interfaces.rest.resources.CreateProductResource;
import com.andeva.atelier.platform.inventory.interfaces.rest.resources.ProductResource;
import com.andeva.atelier.platform.inventory.interfaces.rest.transform.ProductResourceFromAggregateAssembler;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/inventory/products")
public class ProductsController {
    private final ProductCommandService productCommandService;

    public ProductsController(ProductCommandService productCommandService) {
        this.productCommandService = productCommandService;
    }

    @PostMapping
    public ResponseEntity<ProductResource> createProduct(@RequestBody CreateProductResource resource) {
        CreateProductCommand command = new CreateProductCommand(
                new BranchId(java.util.UUID.fromString(resource.branchId())),
                ProductCategory.valueOf(resource.category()),
                new ProductName(resource.name()),
                new Sku(resource.sku()),
                resource.description(),
                new Money(java.math.BigDecimal.valueOf(resource.salePrice())),
                new InventoryQuantity(resource.minimumStock())
        );

        Optional<Product> product = productCommandService.handle(command);
        return product.map(p -> new ResponseEntity<>(ProductResourceFromAggregateAssembler.toResourceFromAggregate(p), HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
