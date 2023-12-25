package com.abbas.securityservice.repository;


import com.abbas.securityservice.domain.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
}
