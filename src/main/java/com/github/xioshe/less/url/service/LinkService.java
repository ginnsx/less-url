package com.github.xioshe.less.url.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xioshe.less.url.api.dto.CreateLinkCommand;
import com.github.xioshe.less.url.api.dto.LinkQuery;
import com.github.xioshe.less.url.api.dto.MigrateResponse;
import com.github.xioshe.less.url.api.dto.Pagination;
import com.github.xioshe.less.url.config.AppProperties;
import com.github.xioshe.less.url.entity.Link;
import com.github.xioshe.less.url.exceptions.CustomAliasDuplicatedException;
import com.github.xioshe.less.url.exceptions.UrlNotFoundException;
import com.github.xioshe.less.url.repository.LinkRepository;
import com.github.xioshe.less.url.shorter.UrlShorter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
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
    private final AccessRecordService accessRecordService;
    private final VisitCountService visitCountService;

    public Link getById(Long id, String ownerId) {
        return linkRepository.getById(id, ownerId)
                .map(link -> {
                    link.setClicks(visitCountService.getVisitCount(link.getShortUrl()));
                    link.addUrlPrefix(appProperties.getBaseUrl());
                    return link;
                })
                .orElseThrow(UrlNotFoundException::new);
    }

    public IPage<Link> query(LinkQuery filters, Pagination page) {
        IPage<Link> pageResult = linkRepository.page(page.toPage(), filters.toQueryWrapper());
        var shortUrls = pageResult.getRecords().stream().map(Link::getShortUrl).toList();
        var counts = visitCountService.batchGetVisitCounts(shortUrls);
        pageResult.getRecords().forEach(link -> {
            link.setClicks(counts.getOrDefault(link.getShortUrl(), 0L));
            link.addUrlPrefix(appProperties.getBaseUrl());
        });
        return pageResult;
    }

    public Link create(CreateLinkCommand command, String ownerId) {
        Link link = createLink(command, ownerId);
        link.addUrlPrefix(appProperties.getBaseUrl());
        return link;
    }

    public Link createLink(CreateLinkCommand command, String ownerId) {
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
        return save(decodedUrl, shortUrl, command.getExpiresAt(), ownerId, useCustom);
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

    public Link save(String originalUrl, String shortUrl,
                     LocalDateTime expiresAt, String ownerId, boolean isCustomAlias) {
        Link record = new Link();
        record.setOriginalUrl(originalUrl);
        record.setShortUrl(shortUrl);
        record.setOwnerId(ownerId);
        record.setStatus(1);
        record.setExpiresAt(expiresAt);
        record.setIsCustom(isCustomAlias);
        linkRepository.save(record);
        return linkRepository.getById(record.getId());
    }

    @Cacheable(cacheNames = "links", key = "#shortUrl")
    public String getOriginalUrl(final String shortUrl) {
        Link link = linkRepository.selectByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException(shortUrl));
        String originalUrl = link.getOriginalUrl();
        if (!StringUtils.hasText(originalUrl)) {
            throw new UrlNotFoundException(shortUrl);
        }
        if (link.getExpiresAt() != null && link.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new UrlNotFoundException(shortUrl);
        }
        return originalUrl;
    }

    public void delete(Long id, String ownerId) {
        linkRepository.getById(id, ownerId).ifPresent(link -> {
            linkRepository.removeById(id);
            Optional.ofNullable(cacheManager.getCache("links"))
                    .ifPresent(cache -> cache.evict(link.getShortUrl()));
        });
    }

    public MigrateResponse migrate(String guestId, String userId) {
        String ownerId = "g_" + guestId;
        var links = linkRepository.selectByOwnerId(ownerId);
        links.forEach(link -> link.setOwnerId("u_" + userId));
        boolean result = linkRepository.updateBatchById(links, 200);
        return new MigrateResponse(result ? links.size() : 0);
    }

    @Async
    public void recordVisit(String url, HttpServletRequest request) {
        // 记录访问记录
        accessRecordService.record(url, request);
        // 统计访问量
        visitCountService.record(url);
    }

    public MigrateResponse countByOwner(String ownerId) {
        var links = linkRepository.lambdaQuery()
                .select(Link::getShortUrl).eq(Link::getOwnerId, ownerId).list();
        if (links.isEmpty()) {
            return new MigrateResponse(0);
        }

        var shortUrls = links.stream().map(Link::getShortUrl).toList();
        var analytics = accessRecordService.countByShortUrls(shortUrls);

        return new MigrateResponse(shortUrls.size(), analytics);
    }
}
