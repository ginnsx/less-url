package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.AccessRecord;
import com.github.xioshe.less.url.repository.mapper.AccessRecordMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AccessRecordRepository extends BaseRepository<AccessRecordMapper, AccessRecord> {

    public long countByShortUrl(String shortUrl) {
        return lambdaQuery().eq(AccessRecord::getShortUrl, shortUrl).count();
    }
}
