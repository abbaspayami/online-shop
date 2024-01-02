package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.entity.Token;
import com.abbas.securityservice.repository.TokenRepository;
import com.abbas.securityservice.service.InMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;

public class RedisStore implements InMemoryStore {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void save(String tokenId, long timeToLeave) {
        var token = new Token();
        token.setTokenId(tokenId);
        token.setTtl(timeToLeave);
        tokenRepository.save(token);
    }
}
