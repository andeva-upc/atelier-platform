package com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.adapters;

import com.andeva.atelier.platform.iam.domain.model.aggregates.User;
import com.andeva.atelier.platform.iam.domain.repositories.UserRepository;
import com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;
import com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.repositories.UserJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(User user) {
        UserPersistenceEntity entity;
        if (user.getId() != null) {
            entity = jpaRepository.findById(user.getId()).orElse(new UserPersistenceEntity());
        } else {
            entity = new UserPersistenceEntity();
            user.setId(UUID.randomUUID());
            entity.setId(user.getId());
        }
        
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPassword());
        entity.setGoogleId(user.getGoogleId());
        entity.setStatus(user.getStatus());
        
        jpaRepository.save(entity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private User toDomain(UserPersistenceEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPasswordHash());
        user.setGoogleId(entity.getGoogleId());
        user.setStatus(entity.getStatus());
        return user;
    }
}
