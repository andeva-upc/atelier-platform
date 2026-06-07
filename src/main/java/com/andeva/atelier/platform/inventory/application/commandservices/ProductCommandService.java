package com.andeva.atelier.platform.inventory.application.commandservices;

import com.andeva.atelier.platform.inventory.domain.model.aggregates.Product;
import com.andeva.atelier.platform.inventory.domain.model.commands.CreateProductCommand;
import java.util.Optional;

public interface ProductCommandService {
    Optional<Product> handle(CreateProductCommand command);
}
