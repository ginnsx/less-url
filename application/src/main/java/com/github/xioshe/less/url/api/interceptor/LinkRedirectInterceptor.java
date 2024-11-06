package com.github.xioshe.less.url.api.interceptor;

import com.github.xioshe.less.url.service.link.LinkService;
import com.github.xioshe.less.url.util.constants.CustomHeaders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class LinkRedirectInterceptor implements HandlerInterceptor {

    private final LinkService linkService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();

        // 排除特定路径
        if (isExcludedPath(path)) {
            return true;  // 继续正常处理
        }

        // 尝试处理为短链
        String shortCode = path.substring(1);  // 移除开头的 /
        String originalUrl = linkService.getOriginalUrl(shortCode);

        if (originalUrl != null) {
            response.setStatus(HttpStatus.FOUND.value());
            response.sendRedirect(originalUrl);
            response.setHeader(HttpHeaders.LOCATION, originalUrl);
            response.setHeader(CustomHeaders.REDIRECT_TYPE, "redirect");
            try {
                linkService.recordVisit(shortCode, request);
            } catch (Exception e) {
                log.error("record access fail", e);
            }
            return false;  // 停止继续处理
        }

        return true;  // 不是短链时继续正常处理
    }

    private boolean isExcludedPath(String path) {
        return path.startsWith("/api/") ||
               path.startsWith("/static/") ||
               path.startsWith("/assets/") ||
               path.startsWith("/favicon.ico") ||
               path.startsWith("/actuator/") ||
               path.startsWith("/doc.html") ||
               path.startsWith("/swagger-ui/") ||
               path.startsWith("/v3/api-docs/");
    }
}
