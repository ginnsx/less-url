package com.github.xioshe.less.url.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * 管理 JWT 使用的 SecretKey，提供密钥轮转功能，提高安全性
 */
@Slf4j
@Component
public class RotatingSecretKeyManager implements InitializingBean {

    private static final int MAX_KEYS = 2;
    private final Deque<SecretKey> keys = new ConcurrentLinkedDeque<>();

    /**
     * JWT 签名密钥，至少 32 个字符（256 位）
     */
    @Value("${security.jwt.key.secret}")
    private String secret;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 支持配置文件中的密钥，可以避免开发时重启后 Token 失效
        if (StringUtils.hasText(secret)) {
            if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
                log.warn("The secret key is too short, it should be at least 32 characters long.");
                throw new IllegalArgumentException("The secret key is too short, it should be at least 32 characters long.");
            }
            keys.offerFirst(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)));
        } else {
            rotateKeys();
        }
    }

    @Scheduled(cron = "${security.jwt.key.rotation.cron:0 0 0 * * ?}")
    public void rotateKeys() {
        log.info("Rotating JWT signing keys");
        keys.offerFirst(generateSecurityKey());
        while (keys.size() > MAX_KEYS) {
            keys.pollLast();
        }
        log.info("JWT signing keys rotated. Current number of active keys: {}", keys.size());
//        jwtMetrics.incrementKeyRotationCount();
    }

    private SecretKey generateSecurityKey() {
        return Jwts.SIG.HS256.key().build();
    }

    public SecretKey getCurrentKey() {
        if (keys.isEmpty()) {
            rotateKeys();
        }
        return keys.peek();
    }

    public Iterable<SecretKey> secretKeys() {
        if (keys.isEmpty()) {
            rotateKeys();
        }
        return keys;
    }

}
