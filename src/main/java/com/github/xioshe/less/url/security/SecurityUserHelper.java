package com.github.xioshe.less.url.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 用于获取当前用户信息的工具类。注册为 Bean 易于单元测试
 */
@Component
public class SecurityUserHelper {

    public Optional<String> getTypedUserId() {
        return getUser().map(SecurityUser::getUserId);
    }

    public String getTypedUserIdOrThrow() {
        return getTypedUserId().orElseThrow(() -> new IllegalStateException("用户未登录"));
    }

    public boolean isGuest() {
        return getUser().map(SecurityUser::isGuest).orElse(false);
    }

    public boolean hasRole(String role) {
        return getUser().map(user -> user.hasRole(role)).orElse(false);
    }

    public Optional<SecurityUser> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Optional.empty();
        }
        if (authentication instanceof GuestAuthentication || authentication instanceof UsernamePasswordAuthenticationToken) {
            return Optional.ofNullable((SecurityUser) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
