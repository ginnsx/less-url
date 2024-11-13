package com.github.xioshe.less.url.rate_limiting.core;

import com.github.xioshe.less.url.rate_limiting.config.RateLimitProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collections;
import java.util.List;

@Slf4j
public class RedisRateLimiter implements RateLimiter {
    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> rateLimitScript;
    private final RateLimitProperties properties;

    public RedisRateLimiter(StringRedisTemplate redisTemplate, RateLimitProperties properties) {
        this.redisTemplate = redisTemplate;
        this.rateLimitScript = new DefaultRedisScript<>();
        this.rateLimitScript.setScriptSource(
                new ResourceScriptSource(
                        new ClassPathResource("scripts/rate_limit.lua")
                )
        );
        this.rateLimitScript.setResultType(Long.class);
        this.properties = properties;
    }

    @Override
    public boolean isAllowed(String key, Double rate, Integer capacity, long now) {
        return tryAcquire(key, rate, capacity, now);
    }

    public boolean tryAcquire(String key, Double rate, Integer capacity, long now) {
        if (!properties.isEnabled()) {
            return true;
        }

        try {
            double actualRate = rate != null ? rate : properties.getDefaultRate();
            int actualCapacity = capacity != null ? capacity : properties.getDefaultCapacity();
            // 计算过期时间：默认为填充满整个令牌桶所需时间的2倍，防止令牌桶过早过期
            long fillTime = (long) Math.ceil((actualCapacity / actualRate) * 2);
            // 使用配置中的最小过期时间
            fillTime = Math.max(fillTime, properties.getMinFillTime());

            List<String> keys = Collections.singletonList(getRateLimitKey(key));
            Object[] args = {
                    String.valueOf(actualRate),
                    String.valueOf(actualCapacity),
                    String.valueOf(now),
                    String.valueOf(fillTime)
            };

            Long result = redisTemplate.execute(
                    rateLimitScript,
                    keys,
                    args
            );

            return result != null && result == 1;
        } catch (Exception e) {
            log.error("Rate limit error for key: {}", key, e);
            return false;
        }
    }

    private String getRateLimitKey(String key) {
        return properties.getDefaultKeyPrefix() + key;
    }
}