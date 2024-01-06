package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.service.InMemoryStore;

public class StoreContextService {

    private final InMemoryStore inMemoryStore;

    public StoreContextService(InMemoryStore inMemoryStore) {
        this.inMemoryStore = inMemoryStore;
    }

    public void processStore(String tokenId, long ttl) {
        inMemoryStore.save(tokenId, ttl);
    }
}
