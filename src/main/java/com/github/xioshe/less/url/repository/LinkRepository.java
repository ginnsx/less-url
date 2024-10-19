package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.Link;
import com.github.xioshe.less.url.repository.mapper.LinkMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LinkRepository extends BaseRepository<LinkMapper, Link> {

    public Optional<Link> getById(Long id, String ownerId) {
        return lambdaQuery()
                .eq(Link::getId, id)
                .eq(ownerId != null, Link::getOwnerId, ownerId)
                .oneOpt();
    }

    public boolean existsByShortUrl(String shortUrl) {
        return lambdaQuery().eq(Link::getShortUrl, shortUrl).exists();
    }


    public Optional<Link> selectByShortUrl(String shortUrl) {
        return lambdaQuery().eq(Link::getShortUrl, shortUrl).oneOpt();
    }

}
