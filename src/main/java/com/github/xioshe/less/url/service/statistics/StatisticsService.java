package com.github.xioshe.less.url.service.statistics;

import com.github.xioshe.less.url.api.dto.statistics.BasicStatsDTO;
import com.github.xioshe.less.url.api.dto.statistics.LocationStatsDTO;
import com.github.xioshe.less.url.api.dto.statistics.MetricCountStatsDTO;
import com.github.xioshe.less.url.api.dto.statistics.TimeSeriesStatsDTO;
import com.github.xioshe.less.url.mapper.VisitStatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final VisitStatsMapper visitStatsMapper;

    public BasicStatsDTO getBasicStats(String shortUrl, LocalDateTime startTime,
                                       LocalDateTime endTime) {
        return visitStatsMapper.getBasicStats(shortUrl, startTime, endTime);
    }

    public List<TimeSeriesStatsDTO> getTimeSeriesStats(String shortUrl, LocalDateTime startTime, LocalDateTime endTime,
                                                       String interval) {
        return visitStatsMapper.getTimeSeriesStats(shortUrl, startTime, endTime, interval);
    }

    public List<LocationStatsDTO> getLocationStats(String shortUrl,
                                                   LocalDateTime startTime, LocalDateTime endTime,
                                                   String type) {
        return switch (type) {
            case "country" -> visitStatsMapper.getCountryStats(shortUrl, startTime, endTime);
            case "region" -> visitStatsMapper.getRegionStats(shortUrl, startTime, endTime);
            case "city" -> visitStatsMapper.getCityStats(shortUrl, startTime, endTime);
            default -> null;
        };
    }

    public List<MetricCountStatsDTO> getMetricCountStats(String shortUrl,
                                                         LocalDateTime startTime, LocalDateTime endTime,
                                                         String metric) {
        return switch (metric) {
            case "referer_type", "referer" -> visitStatsMapper.getRefererStats(shortUrl, startTime, endTime, metric);
            case "language", "timezone" -> visitStatsMapper.getLocaleStats(shortUrl, startTime, endTime, metric);
            case "device", "brand", "device_type" -> visitStatsMapper.getDeviceStats(shortUrl, startTime, endTime, metric);
            case "os", "browser" -> visitStatsMapper.getPlatformStats(shortUrl, startTime, endTime, metric);
            default -> Collections.emptyList();
        };
    }
}