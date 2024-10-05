package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.api.dto.CreateLinkCommand;
import com.github.xioshe.less.url.entity.Link;
import com.github.xioshe.less.url.repository.LinkRepository;
import com.github.xioshe.less.url.shorter.UrlShorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LinkServiceTest {

    private LinkRepository linkRepository;

    private UrlShorter urlShorter;

    private LinkService linkService;

    @BeforeEach
    void setUp() {
        linkRepository = mock(LinkRepository.class);
        urlShorter = mock(UrlShorter.class);
        linkService = new LinkService(linkRepository, urlShorter);
    }

    @Test
    void Shorten_custom_alias() {
        var cmd = new CreateLinkCommand();
        cmd.setUserId(1L);
        cmd.setOriginalUrl("https://example.com");
        cmd.setCustomAlias("custom");

        when(linkRepository.existsByShortUrl("custom")).thenReturn(false);

        var result = linkService.shorten(cmd);
        assertEquals("custom", result);
        verify(linkRepository).save(argThat(url ->
                url.getOriginalUrl().equals("https://example.com")
                && url.getUserId() == 1L
                && url.getShortUrl().equals("custom")
                && url.getExpirationTime() == null));
    }

    @Test
    void Shorten_conflicted_custom_alias() {
        var cmd = new CreateLinkCommand();
        cmd.setUserId(1L);
        cmd.setOriginalUrl("https://example.com");
        cmd.setCustomAlias("custom");

        when(linkRepository.existsByShortUrl("custom")).thenReturn(true);

        var e = assertThrows(RuntimeException.class, () -> linkService.shorten(cmd));
        assertEquals("Alias already exists", e.getMessage());
    }

    @Test
    void Shorten_existing_url() {
        String originalUrl = "https:///existing.com";
        String decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        String shortUrl = "https://existing-short.com";
        var date = LocalDateTime.now();

        when(linkRepository.selectByOriginalUrlAndUserId(decodedUrl, 1L)).thenReturn(Optional.of(shortUrl));

        String result = linkService.shorten(originalUrl, 1L, date);

        assertEquals(shortUrl, result);
        verify(linkRepository, never()).save(any(Link.class));
    }

    @Test
    void Shorten_url() {
        String originalUrl = "https://example.com";
        String decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        String shortUrl = "https://short.com";
        var date = LocalDateTime.now();

        when(linkRepository.selectByOriginalUrlAndUserId(decodedUrl, 1L)).thenReturn(Optional.empty());
        when(urlShorter.shorten(decodedUrl)).thenReturn(shortUrl);
        when(linkRepository.existsByShortUrl(shortUrl)).thenReturn(false);

        String result = linkService.shorten(originalUrl, 1L, date);

        assertEquals(shortUrl, result);
        verify(linkRepository).save(argThat(url ->
                url.getOriginalUrl().equals(decodedUrl)
                && url.getUserId() == 1L
                && url.getShortUrl().equals(shortUrl)
                && url.getExpirationTime() == date));
    }

    @Test
    void Shorten_exceeding_then_fail() {
        String originalUrl = "https://fail.com";
        String decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        String shortUrl = "https://fail-short.com";

        when(linkRepository.selectByOriginalUrlAndUserId(decodedUrl, 1L)).thenReturn(Optional.empty());
        when(urlShorter.shorten(anyString())).thenReturn(shortUrl);
        when(linkRepository.existsByShortUrl(shortUrl)).thenReturn(true);

        var e = assertThrows(RuntimeException.class,
                () -> linkService.shorten(originalUrl, 1L, LocalDateTime.now()));
        assertEquals("Failed to shorten url", e.getMessage());
    }

    @Test
    void Shorten_conflict_once() {
        String originalUrl = "https://conflict.com";
        String decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        String shortUrl = "https://conflict-short.com";
        var date = LocalDateTime.now();

        when(linkRepository.selectByOriginalUrlAndUserId(decodedUrl, 1L)).thenReturn(Optional.empty());
        when(urlShorter.shorten(anyString())).thenReturn(shortUrl);
        when(linkRepository.existsByShortUrl(shortUrl)).thenReturn(true).thenReturn(false);

        String result = linkService.shorten(originalUrl, 1L, date);

        assertEquals(shortUrl, result);
        verify(linkRepository).save(argThat(url ->
                url.getOriginalUrl().equals(decodedUrl)
                && url.getUserId() == 1L
                && url.getShortUrl().equals(shortUrl)
                && url.getExpirationTime() == date));
    }
}
