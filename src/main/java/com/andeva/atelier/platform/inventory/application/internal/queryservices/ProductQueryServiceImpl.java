package com.andeva.atelier.platform.inventory.application.internal.queryservices;
import com.andeva.atelier.platform.inventory.application.queryservices.ProductQueryService;
import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.queries.GetProductByIdQuery;
import com.andeva.atelier.platform.inventory.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class ProductQueryServiceImpl implements ProductQueryService {
    private final ProductRepository productRepository;
    public ProductQueryServiceImpl(ProductRepository productRepository) { this.productRepository = productRepository; }
    @Override public Optional<Product> handle(GetProductByIdQuery query) { return productRepository.findById(query.productId()); }
}
