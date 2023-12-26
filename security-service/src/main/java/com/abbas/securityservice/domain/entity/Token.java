package com.abbas.securityservice.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Token")
@Data

public class Token {

    @Id
    private String tokenId;

    private long ttl;

}