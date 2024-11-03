package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.AccessRecord;
import com.github.xioshe.less.url.entity.Link;
import com.github.xioshe.less.url.mapper.AccessRecordMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AccessRecordRepository extends BaseRepository<AccessRecordMapper, AccessRecord> {

    public List<Link> countByAccessTime(LocalDateTime start, LocalDateTime end) {
        return baseMapper.countByAccessTime(start, end);
    }

    public List<AccessRecord> getAccessRecordsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return lambdaQuery()
                .between(AccessRecord::getAccessTime, startTime, endTime)
                .list();
    }

    public List<AccessRecord> getAccessRecordsBetween(String ownerId, LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.selectAccessRecordsBetween(ownerId, startTime, endTime);
    }
}
