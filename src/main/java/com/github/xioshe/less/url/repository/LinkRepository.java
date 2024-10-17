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

    public Optional<Link> selectByOriginalUrlAndUserId(String originalUrl, Long userId) {
        return lambdaQuery()
                .eq(Link::getOriginalUrl, originalUrl)
                .eq(userId != null, Link::getUserId, userId)
                .oneOpt();
    }


    public Link selectByShortUrl(String shortUrl) {
        return lambdaQuery().eq(Link::getShortUrl, shortUrl).one();
    }

}
