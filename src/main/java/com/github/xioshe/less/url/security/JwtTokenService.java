package com.github.xioshe.less.url.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenService {

    private static final String BLACKLIST_PREFIX = "lu:blacklist:";
    private static final String REFRESH_BLACKLIST_PREFIX = "lu:blacklist:refresh:";

    @Getter
    @Value("${security.jwt.access-expiration-seconds:3600}")
    private long accessTokenExpiration;

    @Getter
    @Value("${security.jwt.refresh-expiration-seconds:86400}")
    private long refreshTokenExpiration;

    /**
     * 用于维护 token 黑名单，实现 jwt 失效的功能
     */
    private final StringRedisTemplate redisTemplate;

    private final RotatingSecretKeyManager keyManager;

//    private final MeterRegistry meterRegistry;


    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, accessTokenExpiration * 1000);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpiration * 1000);
    }

    private String buildToken(Map<String, Object> extraClaims,
                              UserDetails userDetails,
                              long expirationMills) {
        return Jwts.builder()
                .claims()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMills))
                .add(extraClaims)
                .and()
                .signWith(keyManager.getCurrentKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractClaims(token);
        boolean isValid = claims.getSubject().equals(userDetails.getUsername())
               && !claims.getExpiration().before(new Date())
               && !isTokenBlacklisted(token);
//        jwtMetricsTime(System.currentTimeMillis() - startTime);
        return isValid;
    }

    public boolean isRefreshTokenValid(String refreshToken, UserDetails userDetails) {
        try {
            Claims claims = extractClaims(refreshToken);
            return claims.getSubject().equals(userDetails.getUsername())
                   && !claims.getExpiration().before(new Date())
                   && !isTokenBlacklisted(refreshToken);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public void blacklistAccessToken(String token) {
        if (!StringUtils.hasText(token)) {
            return;
        }
        String username = extractUsername(token);
        long ttl = extractExpiration(token).getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            log.info("Access token blacklisted for user: {}", username);
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + username, token, ttl, TimeUnit.MILLISECONDS);
//            meterRegistry.counter("jwt.blacklist.count").increment();
        }
    }

    public void blacklistRefreshToken(String token) {
        String username = extractUsername(token);
        long ttl = extractExpiration(token).getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            log.info("Refresh token blacklisted for user: {}", username);
            redisTemplate.opsForValue().set(REFRESH_BLACKLIST_PREFIX + username, token, ttl, TimeUnit.MILLISECONDS);
//            meterRegistry.counter("jwt.blacklist.count").increment();
        }
    }

    private boolean isTokenBlacklisted(String token) {
        String username = extractUsername(token);
        String blacklistedToken = redisTemplate.opsForValue().get(BLACKLIST_PREFIX + username);
        return token.equals(blacklistedToken);
    }

    private Claims extractClaims(String token) {
        JwtException exception = null;
        // 密钥会自动切换，token 对应的密钥可能被换掉了
        for (SecretKey secretKey : keyManager.secretKeys()) {
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
        throw exception;
    }
}

//@Component
//public class JwtMetrics MeterRegistry registry;
//public JwtMetrics(MeterRegistry registry) {
//    this.registry = registry;
//}
//public void incrementRotationCount() {
//    registry.counter("jwt.key.rotation").increment();
//}
//public void recordTokendationTime(long timeInMs) {
//    registry.timer("jwt.token.validation.time").record(timeInMs, TimeUnit.MILLISECONDS);
//}
//}
