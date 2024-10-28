package com.github.xioshe.less.url.service.analysis;

import com.github.xioshe.less.url.entity.AccessRecord;
import com.github.xioshe.less.url.entity.analysis.VisitStats;
import com.github.xioshe.less.url.repository.AccessRecordRepository;
import com.github.xioshe.less.url.repository.analysis.DeviceDimRepository;
import com.github.xioshe.less.url.repository.analysis.GeoDimRepository;
import com.github.xioshe.less.url.repository.analysis.LocaleDimRepository;
import com.github.xioshe.less.url.repository.analysis.PlatformDimRepository;
import com.github.xioshe.less.url.repository.analysis.RefererDimRepository;
import com.github.xioshe.less.url.repository.analysis.VisitStatsRepository;
import com.github.xioshe.less.url.service.analysis.vo.AccessVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class VisitStatsService {

    private final AnalysisService analysisService;
    private final VisitStatsRepository visitStatsRepository;
    private final GeoDimRepository geoDimRepository;
    private final DeviceDimRepository deviceDimRepository;
    private final PlatformDimRepository platformDimRepository;
    private final LocaleDimRepository localeDimRepository;
    private final RefererDimRepository refererDimRepository;
    private final AccessRecordRepository accessRecordRepository;


    @Transactional
    public void analyzeAccessRecords(String ownerId, LocalDateTime startTime, LocalDateTime endTime) {
        // 获取需要处理的访问记录
        List<AccessRecord> records = accessRecordRepository.getAccessRecordsBetween(ownerId, startTime, endTime);
        processAccessRecords(records);
    }

    //    @Retryable(value = {SQLException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    @Transactional
    public void processAccessRecords(List<AccessRecord> records) {
        log.info("Starting to process {} access records", records.size());
        Set<String> uniqueVisitors = new HashSet<>();
        for (AccessRecord record : records) {
            try {
                processRecord(record, uniqueVisitors);
            } catch (Exception e) {
                log.error("Error processing access record: {}", record, e);
            }
        }
        log.info("Finished processing {} access records", records.size());
        log.info("Unique visitors in this batch: {}", uniqueVisitors.size());
    }

    private void processRecord(AccessRecord record, Set<String> uniqueVisitors) {
        log.debug("Processing access record: {}", record);
        var accessInfo = analysisService.analyze(record);
        boolean isUniqueVisitor = isUniqueVisitor(accessInfo, uniqueVisitors);
        VisitStats stats = convertLogToStats(accessInfo, isUniqueVisitor);
        log.debug("Converted access record to visit stats: {}", stats);
        visitStatsRepository.recordStats(stats);
        log.debug("Recorded visit stats for short URL: {}", stats.getShortUrl());
    }

    private VisitStats convertLogToStats(AccessVO info, boolean isUniqueVisitor) {
        log.debug("Converting AccessVO to VisitStats: {}", info);
        VisitStats stats = new VisitStats();
        stats.setShortUrl(info.getShortUrl());
        stats.setVisitTime(info.getAccessTime().truncatedTo(ChronoUnit.HOURS));
        stats.setGeoId(geoDimRepository.getOrCreateGeoId(info.getCountry(), info.getCity(),
                info.getRegion(), info.getContinent()));
        stats.setDeviceId(deviceDimRepository.getOrCreateDeviceId(info.getDevice(), info.getBrand(), info.getDeviceType()));
        stats.setPlatformId(platformDimRepository.getOrCreatePlatformId(info.getOs(), info.getBrowser()));
        stats.setLocaleId(localeDimRepository.getOrCreateLocaleId(info.getTimezone(), info.getLanguage()));
        stats.setRefererId(refererDimRepository.getOrCreateRefererId(info.getRefererType(), info.getReferer()));
        stats.setClicks(1);
        stats.setVisitors(isUniqueVisitor ? 1 : 0);
        log.debug("Converted AccessVO to VisitStats: {}", stats);
        return stats;
    }

    private boolean isUniqueVisitor(AccessVO info, Set<String> uniqueVisitors) {
        // Create a unique identifier for the visitor (e.g., IP + UserAgent)
        String visitorId = info.getIp() + "|" + info.getUserAgent();
        boolean isUnique = uniqueVisitors.add(visitorId);
        log.debug("Visitor {} is {}", visitorId, isUnique ? "unique" : "returning");
        return isUnique;
    }
}
