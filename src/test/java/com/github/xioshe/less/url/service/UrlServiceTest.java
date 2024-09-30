package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.api.dto.CreateUrlCommand;
import com.github.xioshe.less.url.entity.Url;
import com.github.xioshe.less.url.repository.UrlRepository;
import com.github.xioshe.less.url.shorter.UrlShorter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
public class UrlServiceTest {

    private UrlRepository urlRepository;

    private UrlShorter urlShorter;

    private UrlService urlService;

    @BeforeEach
    void setUp() {
        urlRepository = mock(UrlRepository.class);
        urlShorter = mock(UrlShorter.class);
        urlService = new UrlService(urlRepository, urlShorter);
    }

    @Test
    void Shorten_custom_alias() {
        var cmd = new CreateUrlCommand();
        cmd.setUserId(1L);
        cmd.setOriginalUrl("https://example.com");
        cmd.setCustomAlias("custom");

        when(urlRepository.existsByShortUrl("custom")).thenReturn(false);

        var result = urlService.shorten(cmd);
        assertEquals("custom", result);
        verify(urlRepository).insert(argThat(url ->
                url.getOriginalUrl().equals("https://example.com")
                && url.getUserId() == 1L
                && url.getShortUrl().equals("custom")
                && url.getExpirationTime() == null));
    }

    @Test
    void Shorten_conflicted_custom_alias() {
        var cmd = new CreateUrlCommand();
        cmd.setUserId(1L);
        cmd.setOriginalUrl("https://example.com");
        cmd.setCustomAlias("custom");

        when(urlRepository.existsByShortUrl("custom")).thenReturn(true);

        var e = assertThrows(RuntimeException.class, () -> urlService.shorten(cmd));
        assertEquals("Alias already exists", e.getMessage());
    }

    //    @Test
    void Shorten_invalid_url() {
        String originalUrl = "invalid-url";

        var e = assertThrows(IllegalArgumentException.class, () -> urlService.shorten(originalUrl, 1L, new Date()));
        assertEquals("Invalid URL", e.getMessage());
    }

    @Test
    void Shorten_existing_url() {
        String originalUrl = "http://existing.com";
        String decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        String shortUrl = "http://existing-short.com";
        var date = new Date();

        when(urlRepository.selectByOriginalUrlAndUserId(decodedUrl, 1L)).thenReturn(shortUrl);

        String result = urlService.shorten(originalUrl, 1L, date);

        assertEquals(shortUrl, result);
        verify(urlRepository, never()).insert(any(Url.class));
    }

    @Test
    void Shorten_url() {
        String originalUrl = "http://example.com";
        String decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        String shortUrl = "http://short.com";
        var date = new Date();

        when(urlRepository.selectByOriginalUrlAndUserId(decodedUrl, 1L)).thenReturn(null);
        when(urlShorter.shorten(decodedUrl)).thenReturn(shortUrl);
        when(urlRepository.existsByShortUrl(shortUrl)).thenReturn(false);

        String result = urlService.shorten(originalUrl, 1L, date);

        assertEquals(shortUrl, result);
        verify(urlRepository).insert(argThat(url ->
                url.getOriginalUrl().equals(decodedUrl)
                && url.getUserId() == 1L
                && url.getShortUrl().equals(shortUrl)
                && url.getExpirationTime() == date));
    }

    @Test
    void Shorten_exceeding_then_fail() {
        String originalUrl = "http://fail.com";
        String decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        String shortUrl = "http://fail-short.com";

        when(urlRepository.selectByOriginalUrlAndUserId(decodedUrl, 1L)).thenReturn(null);
        when(urlShorter.shorten(anyString())).thenReturn(shortUrl);
        when(urlRepository.existsByShortUrl(shortUrl)).thenReturn(true);

        var e = assertThrows(RuntimeException.class,
                () -> urlService.shorten(originalUrl, 1L, new Date()));
        assertEquals("Failed to shorten url", e.getMessage());
    }

    @Test
    void Shorten_conflict_once() {
        String originalUrl = "http://conflict.com";
        String decodedUrl = URLDecoder.decode(originalUrl, StandardCharsets.UTF_8);
        String shortUrl = "http://conflict-short.com";
        var date = new Date();

        when(urlRepository.selectByOriginalUrlAndUserId(decodedUrl, 1L)).thenReturn(null);
        when(urlShorter.shorten(anyString())).thenReturn(shortUrl);
        when(urlRepository.existsByShortUrl(shortUrl)).thenReturn(true).thenReturn(false);

        String result = urlService.shorten(originalUrl, 1L, date);

        assertEquals(shortUrl, result);
        verify(urlRepository).insert(argThat(url ->
                url.getOriginalUrl().equals(decodedUrl)
                && url.getUserId() == 1L
                && url.getShortUrl().equals(shortUrl)
                && url.getExpirationTime() == date));
    }
}
