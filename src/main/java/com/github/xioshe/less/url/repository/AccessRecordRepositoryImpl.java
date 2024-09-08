package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.AccessRecord;
import com.github.xioshe.less.url.repository.mapper.AccessRecordMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccessRecordRepositoryImpl implements AccessRecordRepository {

    private final AccessRecordMapper accessRecordMapper;

    @Override
    public void save(AccessRecord accessRecord) {
        accessRecordMapper.insert(accessRecord);
    }

    @Override
    public int countByShortUrl(String shortUrl) {
        return accessRecordMapper.countByShortUrl(shortUrl);
    }
}
