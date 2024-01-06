package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.entity.Token;
import com.abbas.securityservice.repository.TokenRepository;
import com.abbas.securityservice.service.InMemoryStore;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
@SuppressWarnings({"unused"})
public class RedisStore implements InMemoryStore {

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void save(String tokenId, long timeToLeave) {
        log.debug("storing token Id into the redis");
        var token = new Token();
        token.setTokenId(tokenId);
        token.setTtl(timeToLeave);
        tokenRepository.save(token);
    }
}
