package com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.iam.domain.model.entities.PasswordRecoveryToken;
import com.andeva.atelier.platform.iam.domain.repositories.PasswordRecoveryTokenRepository;
import com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.assemblers.PasswordRecoveryTokenPersistenceAssembler;
import com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.entities.PasswordRecoveryTokenPersistenceEntity;
import com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.repositories.PasswordRecoveryTokenPersistenceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PasswordRecoveryTokenRepositoryImpl implements PasswordRecoveryTokenRepository {

    private final PasswordRecoveryTokenPersistenceRepository jpaRepository;

    public PasswordRecoveryTokenRepositoryImpl(PasswordRecoveryTokenPersistenceRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(PasswordRecoveryToken token) {
        PasswordRecoveryTokenPersistenceEntity entity;
        if (token.getId() != null) {
            entity = jpaRepository.findById(token.getId()).orElse(new PasswordRecoveryTokenPersistenceEntity());
        } else {
            entity = new PasswordRecoveryTokenPersistenceEntity();
        }
        
        PasswordRecoveryTokenPersistenceAssembler.toEntity(token, entity);
        jpaRepository.save(entity);
    }

    @Override
    public Optional<PasswordRecoveryToken> findByTokenHash(String tokenHash) {
        return jpaRepository.findByTokenHash(tokenHash).map(PasswordRecoveryTokenPersistenceAssembler::toDomain);
    }
}
