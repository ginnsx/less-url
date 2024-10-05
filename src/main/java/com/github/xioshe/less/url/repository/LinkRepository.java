package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.Link;
import com.github.xioshe.less.url.repository.mapper.LinkMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LinkRepository extends BaseRepository<LinkMapper, Link> {

    public boolean existsByShortUrl(String shortUrl) {
        return lambdaQuery().eq(Link::getShortUrl, shortUrl).exists();
    }

    public Optional<String> selectByOriginalUrlAndUserId(String originalUrl, Long userId) {
        return lambdaQuery()
                .select(Link::getShortUrl)
                .eq(Link::getOriginalUrl, originalUrl)
                .eq(Link::getUserId, userId)
                .oneOpt()
                .map(Link::getShortUrl);
    }


    public Link selectByShortUrl(String shortUrl) {
        return lambdaQuery().eq(Link::getShortUrl, shortUrl).one();
    }

}
