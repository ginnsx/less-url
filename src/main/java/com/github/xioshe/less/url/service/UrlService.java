package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.api.CreateUrlCommand;
import com.github.xioshe.less.url.repository.UrlRepository;
import com.github.xioshe.less.url.shorter.UrlShorter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final UrlShorter urlShorter;

    public String shorten(CreateUrlCommand command) {
        String decodedUrl = URLDecoder.decode(command.getOriginalUrl(), StandardCharsets.UTF_8);
        String customAlias = command.getCustomAlias();
        if (StringUtils.hasText(customAlias)) {
            if (urlRepository.existShortUrl(customAlias)) {
                throw new IllegalArgumentException("Alias already exists");
            }
            urlRepository.save(decodedUrl, customAlias, command.getUserId());
            return customAlias;
        }
        return shorten(decodedUrl, command.getUserId());
    }

    public String shorten(String originalUrl, Long userId) {
        String existedShort = urlRepository.getShortUrl(originalUrl, userId);
        if (existedShort != null) {
            return existedShort;
        }

        // 构建，检查并解决冲突
        String shortUrl = originalUrl;
        // 最多尝试 3 次，尽最大努力解决冲突
        for (int i = 0; i < 3; i++) {
            shortUrl = urlShorter.shorten(shortUrl);
            if (!urlRepository.existShortUrl(shortUrl)) {
                urlRepository.save(originalUrl, shortUrl, userId);
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
