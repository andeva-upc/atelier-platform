package com.andeva.atelier.platform.iam.application.commandservices;

import com.andeva.atelier.platform.iam.domain.model.aggregates.User;
import com.andeva.atelier.platform.iam.domain.model.commands.SignInCommand;
import com.andeva.atelier.platform.iam.domain.model.commands.SignUpCommand;
import com.andeva.atelier.platform.iam.domain.model.queries.AuthenticatedUser;

import java.util.Optional;

public interface UserCommandService {
    Optional<User> handle(SignUpCommand command);
    Optional<AuthenticatedUser> handle(SignInCommand command);
}
