package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.service.AccessRecordService;
import com.github.xioshe.less.url.service.UrlService;
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


@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class RedirectController {

    private final UrlService urlService;
    private final AccessRecordService accessRecordService;

    @GetMapping("{shortUrl}")
    public ResponseEntity<String> redirect(@PathVariable String shortUrl, HttpServletRequest request) {
        log.info(shortUrl);
        String url = urlService.getOriginalUrl(shortUrl);
        try {
            accessRecordService.save(url, request);
        } catch (Exception e) {
            log.error("record access fail", e);
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(url))
                .header(RedirectHeader.REDIRECT_TYPE, "redirect")
                .cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePublic().mustRevalidate())
                .body("Redirecting...");
    }
}
