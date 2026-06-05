package com.andeva.atelier.platform.iam.application.internal.commandservices;

import com.andeva.atelier.platform.iam.application.internal.outboundservices.HashingService;
import com.andeva.atelier.platform.iam.application.internal.outboundservices.TokenService;
import com.andeva.atelier.platform.iam.domain.model.aggregates.User;
import com.andeva.atelier.platform.iam.domain.model.commands.SignInCommand;
import com.andeva.atelier.platform.iam.domain.model.commands.SignUpCommand;
import com.andeva.atelier.platform.iam.domain.model.commands.UpdateUserEmailCommand;
import com.andeva.atelier.platform.iam.domain.model.commands.UpdateUserPasswordCommand;
import com.andeva.atelier.platform.iam.domain.model.queries.AuthenticatedUser;
import com.andeva.atelier.platform.iam.domain.repositories.UserRepository;
import com.andeva.atelier.platform.iam.application.commandservices.UserCommandService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.findByEmail(command.email()).isPresent()) {
            throw new IllegalArgumentException("iam.error.email.alreadyInUse");
        }
        var user = new User(command.email(), hashingService.encode(command.password()));
        userRepository.save(user);
        return userRepository.findByEmail(command.email());
    }

    @Override
    public Optional<AuthenticatedUser> handle(SignInCommand command) {
        var user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new IllegalArgumentException("iam.error.credentials.invalid"));

        if (!hashingService.matches(command.password(), user.getPassword())) {
            throw new IllegalArgumentException("iam.error.credentials.invalid");
        }

        var token = tokenService.generateToken(user.getEmail());
        return Optional.of(new AuthenticatedUser(user, token));
    }

    @Override
    public Optional<User> handle(UpdateUserEmailCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("iam.error.user.notFound"));

        if (!user.getEmail().equals(command.newEmail()) && userRepository.findByEmail(command.newEmail()).isPresent()) {
            throw new IllegalArgumentException("iam.error.email.alreadyInUse");
        }

        user.updateEmail(command.newEmail());
        userRepository.save(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> handle(UpdateUserPasswordCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("iam.error.user.notFound"));

        if (!hashingService.matches(command.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("iam.error.currentPassword.invalid");
        }

        user.updatePassword(hashingService.encode(command.newPassword()));
        userRepository.save(user);
        return Optional.of(user);
    }
}
