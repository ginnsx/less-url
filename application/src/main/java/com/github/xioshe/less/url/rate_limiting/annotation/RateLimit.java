package com.github.xioshe.less.url.rate_limiting.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /**
     * 限流唯一标识
     */
    String key() default "";

    /**
     * 令牌生成速率
     */
    double rate() default 1.0;

    /**
     * 限流时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 令牌桶容量
     */
    int capacity() default 1;

    /**
     * 限流策略
     */
    Strategy strategy() default Strategy.IP;

    enum Strategy {
        /** 按IP限流 */
        IP,
        /** 按用户限流 */
        USER,
        /** 按请求路径限流 */
        PATH,
        /** 按自定义key限流 */
        CUSTOM
    }
}