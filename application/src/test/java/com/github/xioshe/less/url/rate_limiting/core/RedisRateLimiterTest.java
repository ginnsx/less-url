package com.github.xioshe.less.url.rate_limiting.core;

import com.github.xioshe.less.url.rate_limiting.config.RateLimitProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@TestPropertySource(properties = "lu.rate-limit.enabled=true")
@SpringBootTest
class RedisRateLimiterTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RateLimitProperties properties;

    private RedisRateLimiter rateLimiter;

    @BeforeEach
    void setUp() {
        rateLimiter = new RedisRateLimiter(redisTemplate, properties);
    }

    @Test
    void testIsAllowed_WithinLimit() {
        String key = "test-key";
        double rate = 2.0;
        int capacity = 2;
        long now = System.currentTimeMillis();

        boolean firstRequest = rateLimiter.isAllowed(key, rate, capacity, now);
        boolean secondRequest = rateLimiter.isAllowed(key, rate, capacity, now);

        assertTrue(firstRequest);
        assertTrue(secondRequest);
    }

    @Test
    void testIsAllowed_ExceedLimit() {
        String key = "test-key-exceed";
        double rate = 1.0;
        int capacity = 1;
        long now = System.currentTimeMillis();

        boolean firstRequest = rateLimiter.isAllowed(key, rate, capacity, now);
        boolean secondRequest = rateLimiter.isAllowed(key, rate, capacity, now);

        assertTrue(firstRequest);
        assertFalse(secondRequest);
    }

    @Test
    void testIsAllowed_WithDefaultValues() {
        String key = "test-key-default";
        long now = System.currentTimeMillis();

        boolean result = rateLimiter.isAllowed(key, null, null, now);

        assertTrue(result);
    }
}