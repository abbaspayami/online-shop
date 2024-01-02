package com.abbas.securityservice.service;

public interface InMemoryStore {

    void save(String tokenId, long timeToLeave);

}
