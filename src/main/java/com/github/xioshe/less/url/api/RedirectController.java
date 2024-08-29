package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.service.UrlService;
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

    @GetMapping("{shortUrl}")
    public ResponseEntity<String> redirect(@PathVariable String shortUrl) {
        log.info(shortUrl);
        String testUrl = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(testUrl))
                .header(RedirectHeader.REDIRECT_TYPE, "redirect")
                .cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePublic().mustRevalidate())
                .body("Redirecting...");
    }
}
