package com.andeva.atelier.platform.inventory.application.internal.commandservices;
import com.andeva.atelier.platform.inventory.application.commandservices.ProductCommandService;
import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.commands.CreateProductCommand;
import com.andeva.atelier.platform.inventory.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class ProductCommandServiceImpl implements ProductCommandService {
    private final ProductRepository productRepository;
    public ProductCommandServiceImpl(ProductRepository productRepository) { this.productRepository = productRepository; }
    @Override public Optional<Product> handle(CreateProductCommand command) {
        if (productRepository.existsBySkuAndBranchId(command.sku(), command.branchId())) throw new IllegalArgumentException("Product with SKU already exists in this branch");
        Product product = new Product(null, command.branchId(), command.category(), command.name(), command.sku());
        productRepository.save(product);
        return Optional.of(product);
    }
}
