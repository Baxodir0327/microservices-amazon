package com.amazon.authservice.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;

@Component
public class JWTProvider {

    @Value("${app.jwt.access.key}")
    private String ACCESS_SECRET;

    @Value("${app.jwt.access.expiration}")
    private long ACCESS_EXPIRATION;

    @Value("${app.jwt.refresh.key}")
    private String REFRESH_SECRET;


    @Value("${app.jwt.refresh.expiration}")
    private long REFRESH_EXPIRATION;

    public String createAccessToken(String id) {
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String createRefreshAccessToken(String id) {
        return Jwts.builder()
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(getSignKeyForRefresh(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(ACCESS_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSignKeyForRefresh() {
        byte[] keyBytes = Decoders.BASE64.decode(REFRESH_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Mono<String> extractUserId(String token) {
        try {
            Jws<Claims> claimsJws = extractClaimsForAccessToken(token);
            return Mono.justOrEmpty(claimsJws.getBody().getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            // Log the exception for debugging purposes
            return Mono.error(new RuntimeException("Invalid JWT token"));
        }
    }

    public String extractUserIdFromRefreshToken(String token) {
        try {
            Jws<Claims> claimsJws = extractClaimsForRefreshToken(token);
            return claimsJws.getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    private Jws<Claims> extractClaimsForAccessToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);
    }

    private Jws<Claims> extractClaimsForRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKeyForRefresh())
                .build()
                .parseClaimsJws(token);
    }

    public boolean isExpired(String token) {
        try {
            extractClaimsForAccessToken(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validRefreshToken(String refreshToken) {
        try {
            extractClaimsForRefreshToken(refreshToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
