package com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.andeva.atelier.platform.iam.domain.model.aggregates.User;
import com.andeva.atelier.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;

import java.util.UUID;

public class UserPersistenceAssembler {

    public static UserPersistenceEntity toEntity(User user, UserPersistenceEntity entity) {
        if (entity == null) {
            entity = new UserPersistenceEntity();
        }
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPassword());
        entity.setGoogleId(user.getGoogleId());
        entity.setStatus(user.getStatus());
        return entity;
    }

    public static User toDomain(UserPersistenceEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPasswordHash());
        user.setGoogleId(entity.getGoogleId());
        user.setStatus(entity.getStatus());
        return user;
    }
}
