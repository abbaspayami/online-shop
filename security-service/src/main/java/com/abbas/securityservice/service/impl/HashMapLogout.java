package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.entity.UserHistory;
import com.abbas.securityservice.repository.UserHistoryRepository;
import com.abbas.securityservice.service.InMemoryStore;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class HashMapLogout implements LogoutHandler {

    private final JwtServiceImpl jwtServiceImpl;
    private final UserHistoryRepository userHistoryRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.debug("starting logout hashmap");
        final String authHeader = request.getHeader("Authorization");
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

        InMemoryStore memoryStore = new HashmapStore();
        StoreContextService hashMap = new StoreContextService(memoryStore);
        hashMap.processStore(userHistory.getTokenId(), timeToExpiration);
        log.debug("ending logout hashmap");
    }
}