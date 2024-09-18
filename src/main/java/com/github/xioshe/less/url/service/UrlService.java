package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.api.dto.CreateUrlCommand;
import com.github.xioshe.less.url.entity.Url;
import com.github.xioshe.less.url.exceptions.UrlNotFoundException;
import com.github.xioshe.less.url.repository.UrlRepository;
import com.github.xioshe.less.url.shorter.UrlShorter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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
            urlRepository.save(decodedUrl, customAlias, command.getExpirationTime(), command.getUserId());
            return customAlias;
        }
        return shorten(decodedUrl, command.getUserId(), command.getExpirationTime());
    }

    public String shorten(String originalUrl, Long userId, Date expirationTime) {
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
                urlRepository.save(originalUrl, shortUrl, expirationTime, userId);
                return shortUrl;
            }
            shortUrl += userId;
        }
        throw new RuntimeException("Failed to shorten url");
    }

    public String getOriginalUrl(String shortUrl) {
        Url url = urlRepository.getByShortUrl(shortUrl);
        if (url == null) {
            throw new UrlNotFoundException(shortUrl);
        }
        String originalUrl = url.getOriginalUrl();
        if (!StringUtils.hasText(originalUrl)) {
            throw new UrlNotFoundException(shortUrl);
        }
        if (url.getExpirationTime() != null && url.getExpirationTime().before(new Date())) {
            throw new UrlNotFoundException(shortUrl);
        }
        return originalUrl;
    }
}
