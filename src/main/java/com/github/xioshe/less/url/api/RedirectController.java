package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.service.AccessRecordService;
import com.github.xioshe.less.url.service.UrlService;
import com.github.xioshe.less.url.util.constants.CustomHeaders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.concurrent.TimeUnit;


@Tag(name = "短链访问入口")
@Slf4j
@RestController
@RequestMapping("/s")
@RequiredArgsConstructor
public class RedirectController {

    private final UrlService urlService;
    private final AccessRecordService accessRecordService;

    @Operation(summary = "短链重定向", description = "访问短链接，返回重定向响应")
    @ApiResponse(responseCode = "302", description = "重定向到原始链接")
    @ApiResponse(responseCode = "404", description = "短链接不存在")
    @SecurityRequirements
    @GetMapping("{shortUrl}")
    public ResponseEntity<String> redirect(@Parameter(description = "短链接") @PathVariable String shortUrl, HttpServletRequest request) {
        String url = urlService.getOriginalUrl(shortUrl);
        try {
            accessRecordService.save(shortUrl, request);
        } catch (Exception e) {
            log.error("record access fail", e);
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url))
                .header(CustomHeaders.REDIRECT_TYPE, "redirect")
                .cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePublic().mustRevalidate())
                .body("Redirecting...");
    }
}
