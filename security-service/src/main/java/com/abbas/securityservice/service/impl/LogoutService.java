package com.abbas.securityservice.service.impl;


import com.abbas.securityservice.entity.Token;
import com.abbas.securityservice.entity.UserHistory;
import com.abbas.securityservice.repository.TokenRepository;
import com.abbas.securityservice.repository.UserHistoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtServiceImpl jwtServiceImpl;
    private final UserHistoryRepository userHistoryRepository;
    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader= request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);

        String key = jwtServiceImpl.extractId(jwt);
        long timeToExpiration = jwtServiceImpl.getTimeToExpiration(jwt);

        Optional<UserHistory> possibleUserHistory = userHistoryRepository.findByTokenId(key);
        if (possibleUserHistory.isEmpty()) {
            return;
        }

        UserHistory userHistory = possibleUserHistory.get();
        userHistory.setRevoked(true);
        userHistoryRepository.save(userHistory);

        var token = new Token();
        token.setTokenId(userHistory.getTokenId());
        token.setTtl(timeToExpiration);
        tokenRepository.save(token);

    }
}
