package com.github.xioshe.less.url.repository.analysis;

import com.github.xioshe.less.url.entity.analysis.VisitStats;
import com.github.xioshe.less.url.repository.BaseRepository;
import com.github.xioshe.less.url.repository.mapper.VisitStatsMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class VisitStatsRepository extends BaseRepository<VisitStatsMapper, VisitStats> {

    @Transactional
    public void recordStats(VisitStats stats) {
        VisitStats existingStats = this.lambdaQuery()
                .eq(VisitStats::getShortUrl, stats.getShortUrl())
                .eq(VisitStats::getVisitTime, stats.getVisitTime())
                .eq(VisitStats::getGeoId, stats.getGeoId())
                .eq(VisitStats::getDeviceId, stats.getDeviceId())
                .eq(VisitStats::getPlatformId, stats.getPlatformId())
                .eq(VisitStats::getLocaleId, stats.getLocaleId())
                .eq(VisitStats::getRefererId, stats.getRefererId())
                .one();

        if (existingStats != null) {
            existingStats.setClicks(existingStats.getClicks() + stats.getClicks());
            existingStats.setVisitors(existingStats.getVisitors() + stats.getVisitors());
            this.updateById(existingStats);
        } else {
            this.save(stats);
        }
    }
}
