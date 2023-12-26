package com.abbas.securityservice.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String extractUsername(String token);

    String extractId(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateToken(UserDetails userDetails);

    long getTimeToExpiration(String token);
}
