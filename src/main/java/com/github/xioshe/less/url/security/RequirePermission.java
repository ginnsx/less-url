package com.github.xioshe.less.url.security;


import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限辅助注解，自动注册权限，并且基于 Spring Security 检查权限
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('{code}')")
public @interface RequirePermission {

    String code();

    String name() default "";

    String description() default "";
}
