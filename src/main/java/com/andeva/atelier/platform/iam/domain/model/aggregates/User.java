package com.andeva.atelier.platform.iam.domain.model.aggregates;

import com.andeva.atelier.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;
import lombok.Getter;

import com.andeva.atelier.platform.iam.domain.model.valueobjects.EmailAddress;
import com.andeva.atelier.platform.iam.domain.model.valueobjects.GoogleId;
import com.andeva.atelier.platform.iam.domain.model.valueobjects.Password;
import com.andeva.atelier.platform.iam.domain.model.valueobjects.UserId;

/**
 * User aggregate root.
 * Represents an authentication account in the system.
 */
@Getter
public class User extends AbstractDomainAggregateRoot<User> {

    private UserId id;
    private EmailAddress email;
    private Password password;
    private GoogleId googleId;
    private String status;

    public User() {
        this.status = "ACTIVE";
    }

    public User(UserId id, EmailAddress email, Password password, GoogleId googleId, String status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.googleId = googleId;
        this.status = status;
    }

    public User(EmailAddress email, Password password) {
        this();
        this.email = email;
        this.password = password;
    }

    public User(EmailAddress email, Password password, GoogleId googleId) {
        this();
        this.email = email;
        this.password = password;
        this.googleId = googleId;
    }
    
    public void deactivate() {
        this.status = "INACTIVE";
    }

    public void changePassword(Password newPassword) {
        this.password = newPassword;
    }

    public void changeEmail(EmailAddress newEmail) {
        this.email = newEmail;
    }

    public void linkGoogleAccount(GoogleId googleId) {
        this.googleId = googleId;
    }

    public void setUserId(UserId id) {
        this.id = id;
    }
}
