package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.Url;

import java.util.Date;

public interface UrlRepository {

    String getShortUrl(String originalUrl, Long userId);

    void save(String decodedUrl, String shortUrl, Date expirationTime, Long userId);

    boolean existShortUrl(String shortUrl);

    String getOriginalUrl(String shortUrl);

    Url getByShortUrl(String shortUrl);
}
