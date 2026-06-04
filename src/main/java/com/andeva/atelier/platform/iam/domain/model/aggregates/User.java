package com.andeva.atelier.platform.iam.domain.model.aggregates;

import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * User aggregate root.
 * Represents an authentication account in the system.
 */
@Getter
public class User extends AbstractDomainAggregateRoot<User> {

    @Setter
    private UUID id;

    @Setter
    private String email;

    @Setter
    private String password;

    @Setter
    private String googleId;

    @Setter
    private String status;

    public User() {
        this.status = "ACTIVE";
    }

    public User(String email, String password) {
        this();
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, String googleId) {
        this();
        this.email = email;
        this.password = password;
        this.googleId = googleId;
    }
    
    public void deactivate() {
        this.status = "INACTIVE";
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
