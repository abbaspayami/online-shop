package com.abbas.securityservice.service.impl;

import com.abbas.securityservice.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@SuppressWarnings({"unused"})
public class JwtServiceImpl implements JwtService {

    @Value("${secret.key}")
    private String secretKey;

    @Value("${token.time}")
    private String tokenTime;

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public String extractId(String token) {

        return extractClaim(token, Claims::getId);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        log.debug("generating token");
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", getRolesFromAuthorities(userDetails.getAuthorities()));
        return generateToken(claims, userDetails);
    }

    private List<String> getRolesFromAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        log.debug("generating token with extra Claims");
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(tokenTime)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public long getTimeToExpiration(String token) {
        Date expiration = extractExpiration(token);
        Date currentTime = new Date();

        if (expiration != null && expiration.after(currentTime)) {
            return expiration.getTime() - currentTime.getTime();
        } else {
            return 0; // Token is either already expired or has no expiration set
        }
    }


}
