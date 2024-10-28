package com.github.xioshe.less.url.shorter;

import com.github.xioshe.less.url.service.link.HashEncodingUrlShorter;
import com.github.xioshe.less.url.util.codec.Encoder;
import com.github.xioshe.less.url.util.hash.HashFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HashEncodingUrlShorterTest {

    private Encoder encoder;
    private HashFunction hashFunction;
    private HashEncodingUrlShorter urlShorter;

    @BeforeEach
    void setUp() {
        encoder = Mockito.mock(Encoder.class);
        hashFunction = Mockito.mock(HashFunction.class);
        urlShorter = new HashEncodingUrlShorter(encoder, hashFunction);
    }

    @Test
    void Shorten() {
        String originalUrl = "http://example.com";
        long hash = 123456789L;
        String encodedUrl = "abcdef";

        when(hashFunction.hash(originalUrl)).thenReturn(hash);
        when(encoder.encode(hash)).thenReturn(encodedUrl);

        String result = urlShorter.shorten(originalUrl);

        assertEquals("abcdef", result);
        verify(hashFunction).hash(originalUrl);
        verify(encoder).encode(hash);
    }

    @Test
    void Shorten_with_substr() {
        String originalUrl = "http://example.com";
        long hash = 123456789L;
        String encodedUrl = "abcdefg";

        when(hashFunction.hash(originalUrl)).thenReturn(hash);
        when(encoder.encode(hash)).thenReturn(encodedUrl);

        String result = urlShorter.shorten(originalUrl);

        assertEquals("abcdef", result);
        verify(hashFunction).hash(originalUrl);
        verify(encoder).encode(hash);
    }

    @Test
    void testShorten_empty_rul() {
        String originalUrl = "";

        var e = assertThrows(IllegalArgumentException.class, () -> urlShorter.shorten(originalUrl));
        assertEquals("originalUrl cannot be null or empty", e.getMessage());
    }
}