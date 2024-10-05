package com.github.xioshe.less.url.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.metrics.cache.CacheMetricsRegistrar;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Configuration
@RequiredArgsConstructor
public class EnableCacheMetrics {

    private final CacheMetricsRegistrar cacheMetricsRegistrar;
    private final CacheManager cacheManager;

    @EventListener(ApplicationStartedEvent.class)
    public void addCachesMetrics() {
        cacheMetricsRegistrar.bindCacheToRegistry(cacheManager.getCache("emails"));
        cacheMetricsRegistrar.bindCacheToRegistry(cacheManager.getCache("names"));
        cacheMetricsRegistrar.bindCacheToRegistry(cacheManager.getCache("links"));
    }
}
