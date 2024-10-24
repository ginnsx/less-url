package com.github.xioshe.less.url.util.lock;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 锁的 key，支持 SpEL 表达式，默认为空，表示使用方法名
     */
    String key();

    /**
     * key 前缀，默认为空，表示不使用前缀
     */
    String prefix() default "";

    /**
     * SpEL 表达式，用于计算 key，默认为空，表示使用 key 属性值
     */
    String spel() default "";

    /**
     * 等待时间，单位秒，默认为 0，表示不等待
     */
    int waitTime() default 0;

    /**
     * 持有时间，单位秒，默认为 0，会使用看门狗自动续期
     */
    int leaseTime() default 0;

    TimeUnit timeunit() default TimeUnit.SECONDS;

    /**
     * 无效字段
     */
    boolean fair() default false;

    /**
     * 无效字段
     */
    boolean autoUnlock() default true;

    /**
     * 无效字段
     */
    boolean ignoreException() default false;

}
