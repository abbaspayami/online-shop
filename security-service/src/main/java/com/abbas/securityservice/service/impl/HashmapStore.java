package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.service.InMemoryStore;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"unused"})
@Log4j2
public class HashmapStore implements InMemoryStore {
        Map<String, Long> map = new HashMap<>();
    @Override
    public void save(String tokenId, long timeToLeave) {
        log.debug("putting tokenId into the hashmap");
        map.put(tokenId, timeToLeave);
    }

    public void remove(String key) {
        log.debug("removing tokenId into the hashmap");
        map.remove(key);
    }

    @Scheduled(fixedRate = 60000)
    private void cleanupExpiredEntries() {
        log.debug("starting cleanup tokenId based on Time To leave");
        long currentTime = System.currentTimeMillis();
        Set<String> keysToRemove = map.keySet().stream()
                .filter(key -> currentTime > map.get(key))
                .collect(Collectors.toSet());

        keysToRemove.forEach(this::remove);
    }
}
