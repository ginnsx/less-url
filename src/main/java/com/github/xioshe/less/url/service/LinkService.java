package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.api.dto.CreateLinkCommand;
import com.github.xioshe.less.url.entity.Link;
import com.github.xioshe.less.url.exceptions.CustomAliasDuplicatedException;
import com.github.xioshe.less.url.exceptions.UrlNotFoundException;
import com.github.xioshe.less.url.repository.LinkRepository;
import com.github.xioshe.less.url.shorter.UrlShorter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final UrlShorter urlShorter;
    private final CacheManager cacheManager;

    public Link shorten(CreateLinkCommand command, Long userId) {
        String decodedUrl = URLDecoder.decode(command.getOriginalUrl(), StandardCharsets.UTF_8);
        String customAlias = command.getCustomAlias();
        if (StringUtils.hasText(customAlias)) {
            if (linkRepository.existsByShortUrl(customAlias)) {
                throw new CustomAliasDuplicatedException(customAlias);
            }
            return save(decodedUrl, customAlias, command.getExpiresAt(), userId, true);
        }
        return shorten(decodedUrl, userId, command.getExpiresAt());
    }

    public Link shorten(String originalUrl, Long userId, LocalDateTime expiresAt) {
        // 依靠 userId 索引能保证 ms 级别查询效率
        Optional<Link> existed = linkRepository.selectByOriginalUrlAndUserId(originalUrl, userId);
        if (existed.isPresent()) {
            return existed.get();
        }

        // 构建，检查并解决冲突
        String shortUrl = originalUrl;
        // 最多尝试 3 次，尽最大努力解决冲突
        for (int i = 0; i < 3; i++) {
            shortUrl = urlShorter.shorten(shortUrl);
            if (!linkRepository.existsByShortUrl(shortUrl)) {
                return save(originalUrl, shortUrl, expiresAt, userId, false);
            }
            shortUrl += userId;
        }
        throw new RuntimeException("Failed to shorten url");
    }


    public Link save(String originalUrl, String shortUrl, LocalDateTime expiresAt, Long userId, boolean isCustomAlias) {
        Link record = new Link();
        record.setOriginalUrl(originalUrl);
        record.setShortUrl(shortUrl);
        record.setUserId(userId);
        record.setStatus(1);
        record.setExpiresAt(expiresAt);
        record.setIsCustom(isCustomAlias);
        linkRepository.save(record);
        return record;
    }

    @Cacheable(cacheNames = "links", key = "#shortUrl")
    public String getOriginalUrl(String shortUrl) {
        Link link = linkRepository.selectByShortUrl(shortUrl);
        if (link == null) {
            throw new UrlNotFoundException(shortUrl);
        }
        String originalUrl = link.getOriginalUrl();
        if (!StringUtils.hasText(originalUrl)) {
            throw new UrlNotFoundException(shortUrl);
        }
        if (link.getExpiresAt() != null && link.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlNotFoundException(shortUrl);
        }
        return originalUrl;
    }

    public void delete(Long id) {
        linkRepository.getOptById(id).ifPresent(link -> {
            linkRepository.removeById(id);
            Optional.ofNullable(cacheManager.getCache("links"))
                    .ifPresent(cache -> cache.evict(link.getShortUrl()));
        });
    }
}
