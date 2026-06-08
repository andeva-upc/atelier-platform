package com.andeva.atelier.platform.inventory.interfaces.rest;

import com.andeva.atelier.platform.inventory.application.commandservices.ProductCommandService;
import com.andeva.atelier.platform.inventory.application.queryservices.ProductQueryService;
import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.commands.CreateProductCommand;
import com.andeva.atelier.platform.inventory.domain.model.queries.GetProductsByBranchIdQuery;
import com.andeva.atelier.platform.inventory.domain.model.valueobjects.*;
import com.andeva.atelier.platform.inventory.interfaces.rest.resources.CreateProductResource;
import com.andeva.atelier.platform.inventory.interfaces.rest.resources.ProductResource;
import com.andeva.atelier.platform.inventory.interfaces.rest.transform.ProductResourceFromAggregateAssembler;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.Money;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing inventory products.
 * Uses CQRS pattern: delegates commands to ProductCommandService
 * and queries to ProductQueryService.
 * @author Adiel Sanchez
 */
@RestController
@RequestMapping(value = "/api/v1/inventory/products", produces = "application/json")
@Tag(name = "Inventory Products", description = "Endpoints for managing products in the inventory")
public class ProductsController {
    private final ProductCommandService productCommandService;
    private final ProductQueryService productQueryService;

    public ProductsController(ProductCommandService productCommandService,
                              ProductQueryService productQueryService) {
        this.productCommandService = productCommandService;
        this.productQueryService = productQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new Product", description = "Creates a new product in the inventory for a specific branch")
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

    @GetMapping("/branch/{branchId}")
    @Operation(summary = "Get all products for a branch", description = "Retrieves all products in the inventory belonging to the specified branch (multi-tenant query)")
    public ResponseEntity<List<ProductResource>> getProductsByBranch(@PathVariable UUID branchId) {
        var query = new GetProductsByBranchIdQuery(new BranchId(branchId));
        List<Product> products = productQueryService.handle(query);
        List<ProductResource> resources = products.stream()
                .map(ProductResourceFromAggregateAssembler::toResourceFromAggregate)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
