package com.abbas.securityservice.service;

public interface StoreContextService {

    void processStore(String tokenId, long ttl);

}
