package com.github.xioshe.less.url.task;

import com.github.xioshe.less.url.entity.AccessRecord;
import com.github.xioshe.less.url.repository.AccessRecordRepository;
import com.github.xioshe.less.url.service.analysis.VisitStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkVisitAnalysisTask {

    private final VisitStatsService visitStatsService;
    private final AccessRecordRepository accessRecordRepository;

    @Scheduled(cron = "0 5 * * * *") // 每小时执行一次
    public void recordStats() {
        log.info("Starting link visit analysis task");
        try {
            // 获取上一个小时的开始和结束时间
            LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
            LocalDateTime startTime = endTime.minusHours(1);

            List<AccessRecord> records = accessRecordRepository.getAccessRecordsBetween(startTime, endTime);
            // 处理访问记录并更新统计数据
            visitStatsService.processAccessRecords(records);
        } catch (Exception e) {
            log.error("Error occurred while recording stats", e);
        }
    }

}