package com.andeva.atelier.platform.core.domain.model.commands;

import com.andeva.atelier.platform.core.domain.model.valueobjects.UserId;

public record DeleteOwnerCommand(UserId userId) {
}
