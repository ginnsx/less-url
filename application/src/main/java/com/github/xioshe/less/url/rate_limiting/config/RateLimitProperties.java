package com.github.xioshe.less.url.rate_limiting.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "lu.rate-limit")
@Data
public class RateLimitProperties {
    /**
     * 是否启用限流
     */
    private boolean enabled = true;

    /**
     * 默认的 key 前缀
     */
    private String defaultKeyPrefix = "lu:rate_limit:";

    /**
     * 默认令牌生成速率
     */
    private double defaultRate = 1.0;

    /**
     * 令牌桶记录的刷新间隔
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 默认令牌桶容量
     */
    private int defaultCapacity = 1;

    /**
     * 令牌桶记录的最小过期时间
     * 防止令牌桶过早过期导致限流不准确
     */
    private long minFillTime = 60;

}