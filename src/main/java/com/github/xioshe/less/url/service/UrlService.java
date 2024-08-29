package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.repository.UrlRepository;
import com.github.xioshe.less.url.shorter.UrlShorter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlShorter urlShorter;

    public String shorten(String originalUrl, Long userId) {
        try {
            new URL(originalUrl);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL", e);
        }
        String decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        String existedShort = urlRepository.getShortUrl(decodedUrl, userId);
        if (existedShort != null) {
            return existedShort;
        }

        // 构建，检查并解决冲突
        String shortUrl = decodedUrl;
        // 最多尝试 3 次，尽最大努力解决冲突
        for (int i = 0; i < 3; i++) {
            shortUrl = urlShorter.shorten(shortUrl);
            if (!urlRepository.existShortUrl(shortUrl)) {
                urlRepository.save(decodedUrl, shortUrl, userId);
                return shortUrl;
            }
            shortUrl += userId;
        }
        throw new RuntimeException("Failed to shorten url");
    }

    public String getOriginalUrl(String shortUrl) {
        String originalUrl = urlRepository.getOriginalUrl(shortUrl);
        if (originalUrl == null) {
            throw new UrlNotFoundException(shortUrl);
        }
        return originalUrl;
    }
}
