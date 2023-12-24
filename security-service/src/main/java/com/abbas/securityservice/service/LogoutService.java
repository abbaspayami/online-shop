package com.abbas.securityservice.service;

import com.abbas.securityservice.dao.Token;
import com.abbas.securityservice.dao.UserHistory;
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

    private final JwtService jwtService;
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

        String key = jwtService.extractId(jwt);
        long timeToExpiration = jwtService.getTimeToExpiration(jwt);

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

//        redisTemplate.opsForValue().set(key, key, timeToExpiration);
    }
}
