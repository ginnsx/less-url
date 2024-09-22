package com.github.xioshe.less.url.security;


import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        // 有必要预热
        rotateKeys();
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
