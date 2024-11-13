package com.github.xioshe.less.url.rate_limiting.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class IpUtil {
    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    public static String getClientIp(HttpServletRequest request) {
        // 遍历所有可能的请求头
        for (String header : IP_HEADERS) {
            String ip = request.getHeader(header);
            if (StringUtils.hasText(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多个代理的情况，第一个IP为客户端真实IP
                int index = ip.indexOf(",");
                if (index != -1) {
                    return ip.substring(0, index);
                }
                return ip;
            }
        }

        // 如果还是获取不到，则使用远程地址
        return request.getRemoteAddr();
    }
}