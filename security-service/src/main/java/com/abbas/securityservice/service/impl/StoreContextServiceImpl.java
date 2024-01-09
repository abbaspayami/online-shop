package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.service.InMemoryStore;
import com.abbas.securityservice.service.StoreContextService;
import org.springframework.stereotype.Service;

@Service
public class StoreContextServiceImpl implements StoreContextService {

    private final InMemoryStore inMemoryStore;

    public StoreContextServiceImpl(InMemoryStore inMemoryStore) {
        this.inMemoryStore = inMemoryStore;
    }

    public void processStore(String tokenId, long ttl) {
        inMemoryStore.save(tokenId, ttl);
    }
}
