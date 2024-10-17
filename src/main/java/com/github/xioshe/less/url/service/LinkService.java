package com.github.xioshe.less.url.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xioshe.less.url.api.dto.CreateLinkCommand;
import com.github.xioshe.less.url.api.dto.LinkQuery;
import com.github.xioshe.less.url.api.dto.Pagination;
import com.github.xioshe.less.url.config.AppProperties;
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
    private final AppProperties appProperties;

    public Link getById(Long id) {
        return linkRepository.getOptById(id)
                .map(link -> link.addUrlPrefix(appProperties.getBaseUrl()))
                .orElseThrow();
    }

    public IPage<Link> query(LinkQuery filters, Pagination page) {
        IPage<Link> pageResult = linkRepository.page(page.toPage(), filters.toQueryWrapper());
        pageResult.getRecords().forEach(link -> link.addUrlPrefix(appProperties.getBaseUrl()));
        return pageResult;
    }

    public Link create(CreateLinkCommand command, Long userId) {
        Link link = createLink(command, userId);
        link.addUrlPrefix(appProperties.getBaseUrl());
        return link;
    }

    public Link createLink(CreateLinkCommand command, Long userId) {
        String decodedUrl = URLDecoder.decode(command.getOriginalUrl(), StandardCharsets.UTF_8);
        String shortUrl = command.getCustomAlias();
        // 处理自定义短链
        boolean useCustom = false;
        if (StringUtils.hasText(shortUrl)) {
            if (linkRepository.existsByShortUrl(shortUrl)) {
                throw new CustomAliasDuplicatedException(shortUrl);
            }
            useCustom = true;
        }
        // 未指定自定义短链，则自动生成
        if (!useCustom) {
            shortUrl = shorten(decodedUrl);
        }
        if (command.getExpiresAt() == null) {
            command.setExpiresAt(LocalDateTime.now().plusSeconds(appProperties.getLink().getDefaultTimeToLiveSeconds()));
        }
        return save(decodedUrl, shortUrl, command.getExpiresAt(), userId, useCustom);
    }

    public String shorten(String originalUrl) {
        // 构建，检查并解决冲突
        String shortUrl = originalUrl;
        // 最多尝试 3 次，尽最大努力解决冲突
        for (int i = 0; i < 3; i++) {
            shortUrl = urlShorter.shorten(shortUrl);
            if (!linkRepository.existsByShortUrl(shortUrl)) {
                return shortUrl;
            }
            shortUrl += System.currentTimeMillis();
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
