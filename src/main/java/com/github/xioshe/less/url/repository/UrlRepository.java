package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.Url;
import com.github.xioshe.less.url.repository.mapper.UrlMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UrlRepository extends BaseRepository<UrlMapper, Url> {

    public boolean existsByShortUrl(String shortUrl) {
        return lambdaQuery().eq(Url::getShortUrl, shortUrl).exists();
    }

    public Optional<String> selectByOriginalUrlAndUserId(String originalUrl, Long userId) {
        return lambdaQuery()
                .select(Url::getShortUrl)
                .eq(Url::getOriginalUrl, originalUrl)
                .eq(Url::getUserId, userId)
                .oneOpt()
                .map(Url::getShortUrl);
    }


    public Url selectByShortUrl(String shortUrl) {
        return lambdaQuery().eq(Url::getShortUrl, shortUrl).one();
    }

}
