package com.github.xioshe.less.url.repository;

public interface UrlRepository {

    String getShortUrl(String originalUrl, Long userId);

    void save(String decodedUrl, String shortUrl, Long userId);

    boolean existShortUrl(String shortUrl);

    String getOriginalUrl(String shortUrl);
}
