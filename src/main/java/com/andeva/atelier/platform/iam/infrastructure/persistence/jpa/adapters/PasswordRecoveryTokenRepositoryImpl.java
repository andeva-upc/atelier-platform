package com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.iam.domain.model.entities.PasswordRecoveryToken;
import com.andeva.atelier.platform.iam.domain.repositories.PasswordRecoveryTokenRepository;
import com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.entities.PasswordRecoveryTokenPersistenceEntity;
import com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.repositories.PasswordRecoveryTokenJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PasswordRecoveryTokenRepositoryImpl implements PasswordRecoveryTokenRepository {

    private final PasswordRecoveryTokenJpaRepository jpaRepository;

    public PasswordRecoveryTokenRepositoryImpl(PasswordRecoveryTokenJpaRepository jpaRepository) {
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
        
        entity.setId(token.getId());
        entity.setTokenHash(token.getTokenHash());
        entity.setCreatedAt(token.getCreatedAt());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setUsed(token.isUsed());
        entity.setUserId(token.getUserId());
        
        jpaRepository.save(entity);
    }

    @Override
    public Optional<PasswordRecoveryToken> findByTokenHash(String tokenHash) {
        return jpaRepository.findByTokenHash(tokenHash).map(this::toDomain);
    }

    private PasswordRecoveryToken toDomain(PasswordRecoveryTokenPersistenceEntity entity) {
        PasswordRecoveryToken token = new PasswordRecoveryToken();
        token.setId(entity.getId());
        token.setTokenHash(entity.getTokenHash());
        token.setCreatedAt(entity.getCreatedAt());
        token.setExpiresAt(entity.getExpiresAt());
        token.setUsed(entity.isUsed());
        token.setUserId(entity.getUserId());
        return token;
    }
}
