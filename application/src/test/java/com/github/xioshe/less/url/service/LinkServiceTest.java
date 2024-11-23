package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.api.dto.CreateLinkCommand;
import com.github.xioshe.less.url.config.AppProperties;
import com.github.xioshe.less.url.entity.Link;
import com.github.xioshe.less.url.exception.CustomAliasDuplicatedException;
import com.github.xioshe.less.url.exception.UrlNotFoundException;
import com.github.xioshe.less.url.repository.LinkRepository;
import com.github.xioshe.less.url.repository.TaskRepository;
import com.github.xioshe.less.url.service.link.AccessRecordService;
import com.github.xioshe.less.url.service.link.LinkService;
import com.github.xioshe.less.url.service.link.UrlShorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkServiceTest {

    @Mock
    private LinkRepository linkRepository;
    @Mock
    private UrlShorter urlShorter;
    @Mock
    private CacheManager cacheManager;
    @Spy
    private AppProperties appProperties = new AppProperties();
    @Mock
    private AccessRecordService accessRecordService;
    @Mock
    private TaskRepository taskRepository;
    @Spy
    private Clock globalClock = Clock.systemDefaultZone();

    @InjectMocks
    private LinkService linkService;

    private CreateLinkCommand createLinkCommand;

    @BeforeEach
    void setUp() {
        createLinkCommand = new CreateLinkCommand();
        createLinkCommand.setOriginalUrl("https://example.com");
    }

    @Test
    void createLink_with_default_ttl() {
        when(urlShorter.shorten(anyString())).thenReturn("generated");
        when(linkRepository.existsByShortUrl("generated")).thenReturn(false);

        linkService.createLink(createLinkCommand, "u_1");
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
    void createLink_with_expiration_alias_shorten_url() {
        var expiresAt = LocalDateTime.now();
        createLinkCommand.setExpiresAt(expiresAt);

        when(urlShorter.shorten(anyString())).thenReturn("generated");
        when(linkRepository.existsByShortUrl("generated")).thenReturn(false);

        linkService.createLink(createLinkCommand, "u_1");

        verify(linkRepository).save(argThat(link ->
                link.getOriginalUrl().equals("https://example.com")
                && link.getOwnerId().equals("u_1")
                && link.getShortUrl().equals("generated")
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

    @Test
    void createLink_withCustomAlias_shouldSaveLink() {
        createLinkCommand.setCustomAlias("custom");
        when(linkRepository.existsByShortUrl("custom")).thenReturn(false);

        linkService.createLink(createLinkCommand, "u_1");

        verify(linkRepository).save(argThat(link ->
                link.getOriginalUrl().equals("https://example.com")
                && link.getOwnerId().equals("u_1")
                && link.getShortUrl().equals("custom")
                && link.getExpiresAt() != null
                && link.getIsCustom()));
    }

    @Test
    void createLink_withConflictingCustomAlias_shouldThrowException() {
        createLinkCommand.setCustomAlias("custom");
        when(linkRepository.existsByShortUrl("custom")).thenReturn(true);

        assertThrows(CustomAliasDuplicatedException.class, () -> linkService.createLink(createLinkCommand, "u_1"));
    }

    @Test
    void createLink_withoutCustomAlias_shouldGenerateShortUrl() {
        when(urlShorter.shorten(anyString())).thenReturn("generated");
        when(linkRepository.existsByShortUrl("generated")).thenReturn(false);

        linkService.createLink(createLinkCommand, "u_1");

        verify(linkRepository).save(argThat(link ->
                link.getOriginalUrl().equals("https://example.com")
                && link.getOwnerId().equals("u_1")
                && link.getShortUrl().equals("generated")
                && link.getExpiresAt() != null
                && !link.getIsCustom()));
    }

    @Test
    void getOriginalUrl_shouldReturnOriginalUrl() {
        Link link = new Link();
        link.setOriginalUrl("https://example.com");
        link.setExpiresAt(LocalDateTime.now().plusDays(1));
        when(linkRepository.selectByShortUrl("short")).thenReturn(Optional.of(link));

        String result = linkService.getOriginalUrl("short");

        assertEquals("https://example.com", result);
    }

    @Test
    void getOriginalUrl_withExpiredLink_shouldThrowException() {
        Link link = new Link();
        link.setOriginalUrl("https://example.com");
        link.setExpiresAt(LocalDateTime.now().minusDays(1));

        when(linkRepository.selectByShortUrl("short")).thenReturn(Optional.of(link));

        assertThrows(UrlNotFoundException.class, () -> linkService.getOriginalUrl("short"));
    }

    @Test
    void shorten_withConflict_shouldRetryAndSucceed() {
        when(urlShorter.shorten(anyString()))
                .thenReturn("conflict")
                .thenReturn("success");
        when(linkRepository.existsByShortUrl("conflict")).thenReturn(true);
        when(linkRepository.existsByShortUrl("success")).thenReturn(false);

        String result = linkService.shorten("https://example.com");

        assertEquals("success", result);
        verify(urlShorter, times(2)).shorten(anyString());
    }
}
