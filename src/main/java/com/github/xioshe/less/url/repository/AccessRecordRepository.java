package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.AccessRecord;

public interface AccessRecordRepository {
    void save(AccessRecord accessRecord);
    int countByShortUrl(String shortUrl);
}
