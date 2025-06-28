package com.attendify.service.interfaces;

import com.attendify.entity.User;

import java.util.UUID;

public interface JwtService {
    String generateToken(User user);
    String extractEmail(String token);
    UUID extractUserId(String token);
    String extractRole(String token);
    boolean isTokenExpired(String token);
}
