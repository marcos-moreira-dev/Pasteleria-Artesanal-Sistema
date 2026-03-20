package com.pasteleria.auth.application;

public record AuthResponse(
    String accessToken,
    String tokenType,
    long expiresIn,
    String username,
    String role
) {
}


