package com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.billing.domain.model.aggregates.Quote;
import com.andeva.atelier.platform.billing.domain.repositories.QuoteRepository;
import com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.assemblers.QuotePersistenceAssembler;
import com.andeva.atelier.platform.billing.infrastructure.persistence.jpa.repositories.QuotePersistenceRepository;
import com.andeva.atelier.platform.shared.domain.model.valueobjects.BranchId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QuoteRepositoryImpl implements QuoteRepository {

    private final QuotePersistenceRepository persistenceRepository;

    public QuoteRepositoryImpl(QuotePersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Quote save(Quote quote) {
        var entity = QuotePersistenceAssembler.toEntity(quote);
        var savedEntity = persistenceRepository.save(entity);
        return QuotePersistenceAssembler.toAggregate(savedEntity);
    }

    @Override
    public Optional<Quote> findById(UUID id) {
        return persistenceRepository.findById(id)
                .map(QuotePersistenceAssembler::toAggregate);
    }

    @Override
    public List<Quote> findAllByBranchId(BranchId branchId) {
        return persistenceRepository.findAllByBranchId(branchId.value())
                .stream()
                .map(QuotePersistenceAssembler::toAggregate)
                .collect(Collectors.toList());
    }
}
