package com.github.xioshe.less.url.util.lock;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String key();

    String spel() default "";

    int waitTime() default 0;

    int leaseTime() default 30;

    TimeUnit unit() default TimeUnit.SECONDS;

    boolean fair() default false;

    boolean autoUnlock() default true;

    boolean ignoreException() default false;

}
