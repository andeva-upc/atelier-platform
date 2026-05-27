package com.andeva.atelier.platform.inventory.application.queryservices;
import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.queries.GetProductByIdQuery;
import java.util.Optional;
public interface ProductQueryService { Optional<Product> handle(GetProductByIdQuery query); }
