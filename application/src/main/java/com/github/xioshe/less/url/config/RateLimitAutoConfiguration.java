package com.github.xioshe.less.url.config;

import com.github.xioshe.less.url.rate_limiting.config.RateLimitProperties;
import com.github.xioshe.less.url.rate_limiting.core.RateLimiter;
import com.github.xioshe.less.url.rate_limiting.core.RedisRateLimiter;
import com.github.xioshe.less.url.rate_limiting.interceptor.RateLimitInterceptor;
import com.github.xioshe.less.url.security.SecurityUserHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
@ConditionalOnProperty(prefix = "rate.limit", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Slf4j
@RequiredArgsConstructor
public class RateLimitAutoConfiguration {

    private final RateLimitProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public RedisRateLimiter redisRateLimiter(
            StringRedisTemplate redisTemplate) {
        return new RedisRateLimiter(redisTemplate, properties);
    }

    @Bean
    public RateLimitInterceptor rateLimitInterceptor(
            RateLimiter rateLimiter,
            RateLimitProperties properties,
            SecurityUserHelper userHelper) {
        return new RateLimitInterceptor(rateLimiter, properties, userHelper);
    }

    @Configuration
    @RequiredArgsConstructor
    public static class InterceptorConfig implements WebMvcConfigurer {
        private final RateLimitInterceptor rateLimitInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(rateLimitInterceptor)
                    .addPathPatterns("/**")
                    .excludePathPatterns("/static/**", "/error")
                    .order(0);
        }
    }
}