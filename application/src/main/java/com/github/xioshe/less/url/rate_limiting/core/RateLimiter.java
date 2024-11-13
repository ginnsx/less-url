package com.github.xioshe.less.url.rate_limiting.core;

public interface RateLimiter {
    /**
     * 检查是否允许访问
     *
     * @param key 限流键
     * @param rate 速率（每秒）
     * @param capacity 容量
     * @param now 当前时间戳
     *
     * @return true if allowed, false if limited
     */
    boolean isAllowed(String key, Double rate, Integer capacity, long now);
}