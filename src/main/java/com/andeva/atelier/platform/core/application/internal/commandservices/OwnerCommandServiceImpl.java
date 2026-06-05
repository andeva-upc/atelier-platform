package com.andeva.atelier.platform.core.application.internal.commandservices;

import com.andeva.atelier.platform.core.application.commandservices.OwnerCommandService;
import com.andeva.atelier.platform.core.domain.model.aggregates.Owner;
import com.andeva.atelier.platform.core.domain.model.commands.CreateOwnerCommand;
import com.andeva.atelier.platform.core.domain.model.commands.DeleteOwnerCommand;
import com.andeva.atelier.platform.core.domain.model.commands.UpdateOwnerCommand;
import com.andeva.atelier.platform.core.domain.model.valueobjects.Document;
import com.andeva.atelier.platform.core.domain.model.valueobjects.PersonName;
import com.andeva.atelier.platform.core.domain.repositories.OwnerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OwnerCommandServiceImpl implements OwnerCommandService {

    private final OwnerRepository ownerRepository;

    public OwnerCommandServiceImpl(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Optional<Owner> handle(CreateOwnerCommand command) {
        if (ownerRepository.existsByUserId(command.userId())) {
            throw new IllegalArgumentException("Owner profile already exists for this user.");
        }

        var document = new Document(command.documentType(), command.documentNumber());
        var personName = new PersonName(command.firstName(), command.lastName());

        var owner = new Owner(
                command.userId(),
                personName,
                document,
                command.phone()
        );

        ownerRepository.save(owner);
        return ownerRepository.findByUserId(command.userId());
    }

    @Override
    public Optional<Owner> handle(UpdateOwnerCommand command) {
        var result = ownerRepository.findByUserId(command.userId());
        if (result.isEmpty()) throw new IllegalArgumentException("Owner profile does not exist");

        var owner = result.get();
        
        owner.update(
            command.firstName(),
            command.lastName(),
            command.documentType(),
            command.documentNumber(),
            command.phone()
        );

        ownerRepository.save(owner);
        return Optional.of(owner);
    }

    @Override
    public void handle(DeleteOwnerCommand command) {
        var existingOwner = ownerRepository.findByUserId(command.userId());
        if (existingOwner.isEmpty()) {
            throw new IllegalArgumentException("Owner does not exist.");
        }
        
        ownerRepository.delete(existingOwner.get());
    }
}
