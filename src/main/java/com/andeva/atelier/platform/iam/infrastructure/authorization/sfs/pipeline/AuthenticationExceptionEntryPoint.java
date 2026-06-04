package com.andeva.atelier.platform.iam.infrastructure.authorization.sfs.pipeline;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationExceptionEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationExceptionEntryPoint.class);

    @Override
    public void commence(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull AuthenticationException authException)
            throws IOException, ServletException {
        LOGGER.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}
