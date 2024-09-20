package com.github.xioshe.less.url.security;

import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


@Slf4j
@Component
public class DelegatedAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

    @Resource(name = "handlerExceptionResolver")
    private HandlerExceptionResolver exceptionResolver;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.exceptionResolver, "exceptionResolver must be specified");
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        log.debug("handle AuthenticationException with HandlerExceptionResolver, reason: {}",
                authException.getMessage());
        exceptionResolver.resolveException(request, response, null, authException);
    }
}
