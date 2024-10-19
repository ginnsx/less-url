package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.api.dto.CreateLinkCommand;
import com.github.xioshe.less.url.config.AppProperties;
import com.github.xioshe.less.url.exceptions.CustomAliasDuplicatedException;
import com.github.xioshe.less.url.repository.LinkRepository;
import com.github.xioshe.less.url.shorter.UrlShorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LinkServiceTest {

    private LinkRepository linkRepository;

    private UrlShorter urlShorter;

    private LinkService linkService;

    private final AppProperties appProperties = new AppProperties();

    @BeforeEach
    void setUp() {
        linkRepository = mock(LinkRepository.class);
        urlShorter = mock(UrlShorter.class);
        linkService = new LinkService(linkRepository, urlShorter, mock(CacheManager.class), appProperties);
    }

    @Test
    void createLink_with_default_ttl() {
        var cmd = new CreateLinkCommand();
        cmd.setOriginalUrl("https://example.com");

        String expectedShortUrl = "https://short.com";
        when(urlShorter.shorten(cmd.getOriginalUrl())).thenReturn(expectedShortUrl);
        when(linkRepository.existsByShortUrl(expectedShortUrl)).thenReturn(false);

        linkService.createLink(cmd, "u_1");
        verify(linkRepository).save(argThat(link ->
                link.getExpiresAt() != null
        ));
    }

    @Test
    void createLink_custom_alias() {
        var cmd = new CreateLinkCommand();
        cmd.setOriginalUrl("https://example.com");
        cmd.setCustomAlias("custom");

        when(linkRepository.existsByShortUrl("custom")).thenReturn(false);

        linkService.createLink(cmd, "u_1");
        verify(linkRepository).save(argThat(link ->
                link.getOriginalUrl().equals("https://example.com")
                && link.getOwnerId().equals("u_1")
                && link.getShortUrl().equals("custom")
                && link.getExpiresAt() != null
                && link.getIsCustom()));
    }

    @Test
    void createLink_conflicted_custom_alias() {
        var cmd = new CreateLinkCommand();
        cmd.setOriginalUrl("https://example.com");
        cmd.setCustomAlias("custom");

        when(linkRepository.existsByShortUrl("custom")).thenReturn(true);

        var e = assertThrows(CustomAliasDuplicatedException.class, () -> linkService.createLink(cmd, "u_1"));
        assertEquals("自定义短链接已存在", e.getMessage());
    }

    @Test
    void createLink_shorten_url() {
        String originalUrl = "https://example.com";
        var expiresAt = LocalDateTime.now();
        var cmd = new CreateLinkCommand();
        cmd.setOriginalUrl(originalUrl);
        cmd.setExpiresAt(expiresAt);

        String expectedShortUrl = "https://short.com";
        when(urlShorter.shorten(originalUrl)).thenReturn(expectedShortUrl);
        when(linkRepository.existsByShortUrl(expectedShortUrl)).thenReturn(false);

        linkService.createLink(cmd, "u_1");

        verify(linkRepository).save(argThat(link ->
                link.getOriginalUrl().equals(originalUrl)
                && link.getOwnerId().equals("u_1")
                && link.getShortUrl().equals(expectedShortUrl)
                && link.getExpiresAt() == expiresAt
                && !link.getIsCustom()));
    }

    @Test
    void shorten_exceeding_retries_then_fail() {
        String originalUrl = "https://fail.com";

        // 模拟冲突，shortUrl 永远存在
        String existingShort = "https://fail-short.com";
        when(linkRepository.existsByShortUrl(existingShort)).thenReturn(true);
        // 每次都会生成 shortUrl
        when(urlShorter.shorten(anyString())).thenReturn(existingShort);

        var e = assertThrows(RuntimeException.class,
                () -> linkService.shorten(originalUrl));
        assertEquals("Failed to shorten url", e.getMessage());
    }

    @Test
    void shorten_only_conflict_once() {
        String originalUrl = "https://conflict.com";

        String shortUrl = "https://conflict-short.com";
        when(urlShorter.shorten(anyString())).thenReturn(shortUrl);
        // 第一次冲突，第二次不冲突
        when(linkRepository.existsByShortUrl(shortUrl)).thenReturn(true).thenReturn(false);

        var result = linkService.shorten(originalUrl);

        assertEquals(shortUrl, result);
    }
}
