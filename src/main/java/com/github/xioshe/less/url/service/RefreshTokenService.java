package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.security.JwtTokenManager;
import com.github.xioshe.less.url.security.TokenType;
import com.github.xioshe.less.url.util.constants.RedisKeys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final long REFRESH_TOKEN_VALIDITY = 14 * 24 * 60 * 60; // 14 days in seconds

    @Getter
    @Value("${security.jwt.refresh-expiration-seconds:86400}")
    private long refreshTokenExpiration;

    private final JwtTokenManager jwtTokenManager;
    private final RedisTemplate<String, String> redisTemplate;

    public String getRefreshTokenId(String username ) {
        String key = RedisKeys.REFRESH_TOKEN_STORE_PREFIX + username;
        return redisTemplate.opsForValue().get(key);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        String token = jwtTokenManager.buildToken(
                userDetails,
                REFRESH_TOKEN_VALIDITY * 1000,
                Map.of(RedisKeys.TOKEN_TYPE, TokenType.REFRESH.value())
        );

        String tokenId = jwtTokenManager.extractTokenId(token);
        // 将 RefreshToken 信息存储到 Redis
        storeRefreshTokenInfo(tokenId, userDetails.getUsername());

        return token;
    }

    private void storeRefreshTokenInfo(String tokenId, String username) {
        String key = RedisKeys.REFRESH_TOKEN_STORE_PREFIX + username;
        redisTemplate.opsForValue().set(key, tokenId, REFRESH_TOKEN_VALIDITY, TimeUnit.SECONDS);
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            // 复用 JwtTokenService 的 extractClaims 方法
            Claims claims = jwtTokenManager.extractClaims(refreshToken);
            String tokenId = claims.getId();
            String username = claims.getSubject();

            // 检查 RefreshToken 是否在 Redis 中存在且未过期
            String key = RedisKeys.REFRESH_TOKEN_STORE_PREFIX + username;
            String storedTokenId = redisTemplate.opsForValue().get(key);

            return tokenId.equals(storedTokenId) && !isTokenBlacklisted(refreshToken);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenBlacklisted(String refreshToken) {
        return jwtTokenManager.isTokenBlacklisted(refreshToken);
    }

    public void blacklistRefreshToken(String refreshToken) {
        try {
            // 从 Redis 中删除 RefreshToken 信息
            String username  = jwtTokenManager.extractUsername(refreshToken);
            redisTemplate.delete(RedisKeys.REFRESH_TOKEN_STORE_PREFIX + username);

            // 将 RefreshToken 加入黑名单
            jwtTokenManager.blacklistToken(refreshToken);
        } catch (JwtException | IllegalArgumentException e) {
            // 处理异常
        }
    }
}