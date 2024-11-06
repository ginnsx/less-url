package com.github.xioshe.less.url.service.link;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xioshe.less.url.api.dto.CountLinkResponse;
import com.github.xioshe.less.url.api.dto.CreateLinkCommand;
import com.github.xioshe.less.url.api.dto.LinkQuery;
import com.github.xioshe.less.url.api.dto.common.Pagination;
import com.github.xioshe.less.url.config.AppProperties;
import com.github.xioshe.less.url.entity.Link;
import com.github.xioshe.less.url.exceptions.CustomAliasDuplicatedException;
import com.github.xioshe.less.url.exceptions.UrlNotFoundException;
import com.github.xioshe.less.url.mapper.LinkMapper;
import com.github.xioshe.less.url.repository.LinkRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final UrlShorter urlShorter;
    private final CacheManager cacheManager;
    private final AppProperties appProperties;
    private final AccessRecordService accessRecordService;
    private final Clock globalClock;
    private final SqlSessionFactory sqlSessionFactory;

    public Link getById(Long id, String ownerId) {
        return linkRepository.getById(id, ownerId)
                .map(link -> link.addUrlPrefix(appProperties.getShortUrlDomain()))
                .orElseThrow(UrlNotFoundException::new);
    }

    public IPage<Link> query(LinkQuery filters, Pagination page) {
        IPage<Link> pageResult = linkRepository.page(page.toPage(), filters.toQueryWrapper());
        pageResult.getRecords().forEach(link -> link.addUrlPrefix(appProperties.getShortUrlDomain()));
        return pageResult;
    }

    public Link create(CreateLinkCommand command, String ownerId) {
        Link link = createLink(command, ownerId);
        link.addUrlPrefix(appProperties.getShortUrlDomain());
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
            var defaultTtl = appProperties.getLink().getDefaultTimeToLiveSeconds();
            command.setExpiresAt(LocalDateTime.now(globalClock).plusSeconds(defaultTtl));
        }
        return save(decodedUrl, shortUrl, command.getExpiresAt(), ownerId, useCustom);
    }

    public String shorten(String originalUrl) {
        // 构建，检查并解决冲突
        // 允许相同 originalUrl 生成不同的短链，第一次缩短时也要加上随机值，否则多条相同 original 第一次必定冲突
        String shortUrl = originalUrl + System.currentTimeMillis();
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
        if (link.getExpiresAt() != null && link.getExpiresAt().isBefore(LocalDateTime.now(globalClock))) {
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

    public CountLinkResponse migrate(String guestId, String userId) {
        String ownerId = "g_" + guestId;
        var links = linkRepository.selectByOwnerId(ownerId);
        links.forEach(link -> link.setOwnerId("u_" + userId));
        boolean result = linkRepository.updateBatchById(links, 200);
        return new CountLinkResponse(result ? links.size() : 0);
    }

    @Async
    public void recordVisit(String url, HttpServletRequest request) {
        // 记录访问记录
        accessRecordService.record(url, request);
    }

    public CountLinkResponse countByOwner(String ownerId) {
        if (!StringUtils.hasText(ownerId)) {
            return new CountLinkResponse(0);
        }
        var links = linkRepository.lambdaQuery()
                .select(Link::getVisits).eq(Link::getOwnerId, ownerId).list();
        if (links.isEmpty()) {
            return new CountLinkResponse(0);
        }

        var analytics = links.stream().mapToInt(Link::getVisits).sum();
        return new CountLinkResponse(links.size(), analytics);
    }

    public List<BatchResult> batchUpdateVisitCount(List<Link> links) {
        MybatisBatch<Link> mybatisBatch = new MybatisBatch<>(sqlSessionFactory, links);
        MybatisBatch.Method<Link> method = new MybatisBatch.Method<>(LinkMapper.class);
        var result = mybatisBatch.execute(method.get("updateVisitCount"));
        log.debug("Batch update {} link visit count", result.size());
        return result;
    }
}
