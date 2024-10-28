package com.github.xioshe.less.url.security;


import com.github.xioshe.less.url.service.auth.PermissionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PermissionRegistrar implements ApplicationListener<ContextRefreshedEvent> {

    private final PermissionService permissionService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(Controller.class);
        for (Object bean : beans.values()) {
            registerPermissionsForBean(bean);
        }
    }

    private void registerPermissionsForBean(Object bean) {
        // 代理对象无法获取 method 注解，需要用原对象
        Class<?> clazz = AopUtils.getTargetClass(bean);
        for (Method method : clazz.getDeclaredMethods()) {
            RequirePermission annotation = method.getAnnotation(RequirePermission.class);
            if (annotation != null) {
                registerPermission(annotation);
            }
        }
        RequirePermission requirePermission = clazz.getDeclaredAnnotation(RequirePermission.class);
        if (requirePermission != null) {
            registerPermission(requirePermission);
        }
    }

    private void registerPermission(RequirePermission requirePermission) {
        String code = requirePermission.code();
        String name = StringUtils.defaultIfBlank(requirePermission.name(), code);
        String description = StringUtils.defaultIfBlank(requirePermission.description(), name);
        permissionService.createPermissionIfNotExists(code, name, description);
    }
}
