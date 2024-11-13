package com.github.xioshe.less.url.rate_limiting.interceptor;

import com.github.xioshe.less.url.rate_limiting.annotation.RateLimit;
import com.github.xioshe.less.url.rate_limiting.config.RateLimitProperties;
import com.github.xioshe.less.url.rate_limiting.core.RateLimiter;
import com.github.xioshe.less.url.rate_limiting.exception.RateLimitException;
import com.github.xioshe.less.url.rate_limiting.util.IpUtil;
import com.github.xioshe.less.url.security.SecurityUserHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RateLimiter rateLimiter;
    private final RateLimitProperties properties;
    private final SecurityUserHelper securityUserHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!properties.isEnabled() || isStaticResource(handler) || isExcludedPath(request)) {
            return true;
        }

        String key;
        double rate = properties.getDefaultRate();
        int capacity = properties.getDefaultCapacity();
        TimeUnit timeUnit = properties.getTimeUnit();

        // 获取限流注解
        RateLimit rateLimit = getRateLimitAnnotation(handler);
        if (rateLimit != null) {
            key = buildKey(request, rateLimit);
            rate = rateLimit.rate();
            capacity = rateLimit.capacity();
            timeUnit = rateLimit.timeUnit();
        } else {
            // 默认使用 IP 获取限流 key
            key = formatIpKey(request);
        }

        // 转换速率为每秒速率
        double ratePerSecond = convertRateToPerSecond(rate, timeUnit);

        // 尝试获取令牌
        boolean acquired = rateLimiter.isAllowed(
                key,
                ratePerSecond,
                capacity,
                System.currentTimeMillis()
        );

        if (!acquired) {
            throw new RateLimitException("Rate limit exceeded");
        }

        return true;
    }

    private boolean isStaticResource(Object handler) {
        return !(handler instanceof HandlerMethod);
    }

    private boolean isExcludedPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/v3/api-docs") || uri.startsWith("/favicon.ico");
    }

    private String buildKey(HttpServletRequest request, RateLimit rateLimit) {
        return switch (rateLimit.strategy()) {
            case IP -> formatIpKey(request);
            case USER -> "user:" + getCurrentUserId();
            case CUSTOM -> "custom:" + rateLimit.key();
            case PATH -> "path:" + request.getRequestURI();
        };
    }

    private String formatIpKey(HttpServletRequest request) {
        String ip = IpUtil.getClientIp(request);

        if (ip == null || ip.isEmpty()) {
            ip = "unknown";
        } else if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "localhost";
        } else if (ip.contains(":")) {
            ip = ip.replace(":", "_");
        }

        // 处理 IPv6 地址中的冒号
        return "ip:" + ip;
    }

    private String getCurrentUserId() {
        var userId = securityUserHelper.getUserId();
        if (userId == null) {
            throw new RateLimitException("User not logged in");
        }
        return userId;
    }

    /**
     * 获取限流注解，优先获取方法上的注解，如果没有则获取类上的注解
     */
    private RateLimit getRateLimitAnnotation(Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return null;
        }

        // 优先查找方法上的注解
        RateLimit rateLimit = AnnotationUtils.findAnnotation(
                handlerMethod.getMethod(),
                RateLimit.class
        );

        // 如果方法上没有，则查找类上的注解
        if (rateLimit == null) {
            rateLimit = AnnotationUtils.findAnnotation(
                    handlerMethod.getBeanType(),
                    RateLimit.class
            );
        }

        return rateLimit;
    }

    /**
     * 将指定时间单位的速率转换为每秒速率
     */
    private double convertRateToPerSecond(double rate, TimeUnit timeUnit) {
        return switch (timeUnit) {
            case SECONDS -> rate;
            case MINUTES -> rate / 60.0;
            case HOURS -> rate / 3600.0;
            case DAYS -> rate / 86400.0;
            default -> throw new IllegalArgumentException("Unsupported time unit: " + timeUnit);
        };
    }
}