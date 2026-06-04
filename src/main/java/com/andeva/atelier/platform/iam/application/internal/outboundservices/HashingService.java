package com.andeva.atelier.platform.iam.application.internal.outboundservices;

public interface HashingService {
    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
