package com.andeva.atelier.platform.iam.domain.model.commands;

public record ResetPasswordCommand(String token, String newPassword) {
}
