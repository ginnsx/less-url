package com.github.xioshe.less.url.repository;


import com.github.xioshe.less.url.entity.Url;
import com.github.xioshe.less.url.repository.mapper.UrlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UriRepositoryImpl implements UrlRepository {

    private final UrlMapper urlMapper;

    @Override
    public String getShortUrl(String originalUrl, Long userId) {
        // 依靠 userId 索引能保证 ms 级别查询效率
        return urlMapper.selectByOriginalUrlAndUserId(originalUrl, userId);
    }

    @Override
    public void save(String decodedUrl, String shortUrl, Long userId) {
        var record = new Url();
        record.setOriginalUrl(decodedUrl);
        record.setShortUrl(shortUrl);
        record.setUserId(userId);
        record.setStatus(1);
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());
        urlMapper.insert(record);
    }

    @Override
    public boolean existShortUrl(String shortUrl) {
        return urlMapper.existsByShortUrl(shortUrl);
    }

    @Override
    public String getOriginalUrl(String shortUrl) {
        return Optional.ofNullable(urlMapper.selectByShortUrl(shortUrl))
                .map(Url::getOriginalUrl).orElse(null);
    }
}
