package com.github.xioshe.less.url.security;

import com.github.xioshe.less.url.util.constants.RedisKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Clock;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenManager {

    @Getter
    @Value("${security.jwt.access-expiration-seconds:3600}")
    private long accessTokenExpiration;

    /**
     * 用于维护 token 黑名单，实现 jwt 失效的功能
     */
    private final StringRedisTemplate redisTemplate;

    private final RotatingSecretKeyManager secretKeyManager;

    private final Clock globalClock;


    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(userDetails, new HashMap<>());
    }

    public String generateAccessToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        extraClaims.put(RedisKeys.TOKEN_TYPE, TokenType.ACCESS.value());
        return buildToken(userDetails, accessTokenExpiration * 1000, extraClaims);
    }

    public String buildToken(UserDetails userDetails, long expirationMills, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .claims()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(globalClock.millis()))
                .notBefore(new Date(globalClock.millis()))
                .expiration(new Date(globalClock.millis() + expirationMills))
                .id(UUID.randomUUID().toString())
                .add(extraClaims)
                .and()
                .signWith(secretKeyManager.getCurrentKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        }
        try {
            validateToken(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public void validateToken(String token) {
        // parse 的时候就会检测 token 是否过期
        extractClaims(token);
        if (isTokenBlacklisted(token)) {
            throw new JwtException("Token is blacklisted");
        }
    }

    public boolean isTokenBlacklisted(String token) {
        String key = blacklistKeyOf(token);
        String blacklistedToken = redisTemplate.opsForValue().get(key);
        return token.equals(blacklistedToken);
    }

    public void blacklistAccessToken(String token) {
        blacklistToken(token);
    }

    public void blacklistToken(String token) {
        if (!isTokenValid(token)) {
            return;
        }
        String key = blacklistKeyOf(token);
        storeBlacklistedToken(key, token);
    }

    private String blacklistKeyOf(String token) {
        TokenType type = extractTokenType(token);
        if (type == null) {
            throw new IllegalArgumentException("Invalid token type");
        }
        String id = extractTokenId(token);
        return switch (type) {
            case ACCESS -> RedisKeys.ACCESS_TOKEN_BLACKLIST_PREFIX + id;
            case REFRESH -> RedisKeys.REFRESH_TOKEN_BLACKLIST_PREFIX + id;
        };
    }

    private void storeBlacklistedToken(String key, String token) {
        long ttl = extractExpiration(token).getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            log.info("Disable token in redis: {}", key);
            redisTemplate.opsForValue().set(key, token, ttl, TimeUnit.MILLISECONDS);
        }
    }

    @Nullable
    public TokenType extractTokenType(String token) {
        String value = extractClaims(token).get(RedisKeys.TOKEN_TYPE, String.class);
        return TokenType.fromValue(value);
    }

    public String extractTokenId(String token) {
        return extractClaims(token).getId();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public Claims extractClaims(String token) {
        JwtException exception = null;
        // 密钥会自动切换，token 对应的密钥可能被换掉了
        for (SecretKey secretKey : secretKeyManager.secretKeys()) {
            try {
                return Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
            } catch (JwtException e) {
                exception = e;
            }
        }
        assert exception != null;
        log.warn("Failed to extract username from token", exception);
        throw exception;
    }
}
