package com.github.xioshe.less.url.rate_limiting.interceptor;

import com.github.xioshe.less.url.rate_limiting.annotation.RateLimit;
import com.github.xioshe.less.url.rate_limiting.config.RateLimitProperties;
import com.github.xioshe.less.url.rate_limiting.core.RateLimiter;
import com.github.xioshe.less.url.rate_limiting.exception.RateLimitException;
import com.github.xioshe.less.url.security.SecurityUserHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateLimitInterceptorTest {
    @Mock
    private RateLimiter rateLimiter;

    @Mock
    private RateLimitProperties properties;

    @Mock
    private SecurityUserHelper securityUserHelper;

    @Mock
    private HttpServletResponse response;

    private RateLimitInterceptor interceptor;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        request.setRequestURI("/foo"); // 限流拦截器位于 redirect 拦截器之前
        interceptor = new RateLimitInterceptor(rateLimiter, properties, securityUserHelper);
    }

    @Test
    void testPreHandle_WithIpStrategy() throws Exception {
        // 准备测试数据
        HandlerMethod handlerMethod = createHandlerMethodWithAnnotation(RateLimit.Strategy.IP);
        String testIp = "192.168.1.1";

        // Mock 行为
        request.addHeader("X-Forwarded-For", testIp);
        when(properties.isEnabled()).thenReturn(true);
        when(rateLimiter.isAllowed(anyString(), anyDouble(), anyInt(), anyLong())).thenReturn(true);

        // 执行测试
        boolean result = interceptor.preHandle(request, response, handlerMethod);

        // 验证结果
        assertTrue(result);
        verify(rateLimiter).isAllowed(contains("ip:" + testIp), anyDouble(), anyInt(), anyLong());
    }

    @Test
    void testPreHandle_WithUserIdStrategy() throws Exception {
        // 准备测试数据
        HandlerMethod handlerMethod = createHandlerMethodWithAnnotation(RateLimit.Strategy.USER);

        // Mock 行为
        when(properties.isEnabled()).thenReturn(true);
        when(securityUserHelper.getUserId()).thenReturn("testUser");
        when(rateLimiter.isAllowed(anyString(), anyDouble(), anyInt(), anyLong())).thenReturn(true);

        // 执行测试
        boolean result = interceptor.preHandle(request, response, handlerMethod);
        // 验证结果
        assertTrue(result);
        verify(rateLimiter).isAllowed(contains("user:" + "testUser"), anyDouble(), anyInt(), anyLong());
    }

    @Test
    void testPreHandle_WithCustomStrategy() throws Exception {
        // 准备测试数据
        HandlerMethod handlerMethod = createHandlerMethodWithAnnotation(RateLimit.Strategy.CUSTOM);

        // Mock 行为
        when(properties.isEnabled()).thenReturn(true);
        when(rateLimiter.isAllowed(anyString(), anyDouble(), anyInt(), anyLong())).thenReturn(true);

        boolean result = interceptor.preHandle(request, response, handlerMethod);
        assertTrue(result);
        verify(rateLimiter).isAllowed(contains("customKey"), anyDouble(), anyInt(), anyLong());
    }

    @Test
    void testPreHandle_RateLimitExceeded() throws Exception {
        HandlerMethod handlerMethod = createHandlerMethodWithAnnotation(RateLimit.Strategy.IP);

        when(properties.isEnabled()).thenReturn(true);
        when(rateLimiter.isAllowed(anyString(), anyDouble(), anyInt(), anyLong())).thenReturn(false);

        assertThrows(RateLimitException.class, () ->
                interceptor.preHandle(request, response, handlerMethod)
        );
    }

    private HandlerMethod createHandlerMethodWithAnnotation(RateLimit.Strategy strategy) {
        class UserIdController {
            @RateLimit(strategy = RateLimit.Strategy.USER, rate = 1.0, capacity = 1)
            public void testMethod() {
            }
        }

        class IpController {
            @RateLimit(strategy = RateLimit.Strategy.IP, rate = 1.0, capacity = 1)
            public void testMethod() {
            }
        }

        class CustomController {
            @RateLimit(strategy = RateLimit.Strategy.CUSTOM, key = "customKey", rate = 1.0, capacity = 1)
            public void testMethod() {
            }
        }

        Object controller = switch (strategy) {
            case USER -> new UserIdController();
            case IP -> new IpController();
            case CUSTOM -> new CustomController();
            default -> throw new IllegalArgumentException("Invalid strategy: " + strategy);
        };

        try {
            Method method = controller.getClass().getMethod("testMethod");
            return new HandlerMethod(controller, method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}