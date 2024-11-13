package com.github.xioshe.less.url.rate_limiting.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IpUtilTest {

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
    }

    @Test
    void testGetClientIp_FromXForwardedFor() {
        request.addHeader("X-Forwarded-For", "192.168.1.1");
        assertEquals("192.168.1.1", IpUtil.getClientIp(request));
    }

    @Test
    void testGetClientIp_FromXForwardedFor_MultipleIps() {
        request.addHeader("X-Forwarded-For", "192.168.1.1,192.168.1.2");
        assertEquals("192.168.1.1", IpUtil.getClientIp(request));
    }

    @Test
    void testGetClientIp_FromRemoteAddr() {
        request.setRemoteAddr("192.168.1.1");
        assertEquals("192.168.1.1", IpUtil.getClientIp(request));
    }

    @Test
    void testGetClientIp_UnknownHeader() {
        request.addHeader("X-Forwarded-For", "unknown");
        request.setRemoteAddr("192.168.1.1");
        assertEquals("192.168.1.1", IpUtil.getClientIp(request));
    }

}