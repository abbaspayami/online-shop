package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.service.InMemoryStore;

import java.util.HashMap;
import java.util.Map;

public class HashmapStore implements InMemoryStore {
    @Override
    public void save(String tokenId, long timeToLeave) {
        Map<String, String> memoryMap = new HashMap<>();
        memoryMap.put(tokenId, tokenId);
    }
}
