package com.abbas.securityservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "Token",timeToLive = 15000)
@Data

public class Token {

    @Id
    private String tokenId;

    private long ttl;

}
