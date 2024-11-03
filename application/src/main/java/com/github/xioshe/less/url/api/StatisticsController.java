package com.github.xioshe.less.url.api;

import com.github.xioshe.api.response.wrapped.WrappedResponse;
import com.github.xioshe.less.url.api.dto.statistics.AnalysisCommand;
import com.github.xioshe.less.url.api.dto.statistics.BasicStatsDTO;
import com.github.xioshe.less.url.api.dto.statistics.LocationStatsDTO;
import com.github.xioshe.less.url.api.dto.statistics.MetricCountStatsDTO;
import com.github.xioshe.less.url.api.dto.statistics.TimeSeriesStatsDTO;
import com.github.xioshe.less.url.service.analysis.VisitStatsService;
import com.github.xioshe.less.url.service.statistics.StatisticsService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@WrappedResponse
@Tag(name = "访问统计")
@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final VisitStatsService visitStatsService;
    private final StatisticsService statisticsService;
    private final Clock systemClock;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/analysis")
    public void analyzeAccessRecords(@RequestBody AnalysisCommand cmd) {
        var startTime = cmd.getStartTime() == null
                ? LocalDateTime.of(1970, 1, 1, 0, 0)
                : cmd.getStartTime();
        var currentHour = LocalDateTime.now(systemClock).truncatedTo(ChronoUnit.HOURS);
        // 不会统计当前小时的数据，由定时任务处理这部分数据
        var endTime = cmd.getEndTime() == null || cmd.getEndTime().isAfter(currentHour)
                ? currentHour : cmd.getEndTime();
        visitStatsService.analyzeAccessRecords(cmd.getOwnerId(), startTime, endTime);
    }

    @GetMapping("/basic")
    public BasicStatsDTO getBasicStats(
            @RequestParam(required = false) String shortUrl,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        return statisticsService.getBasicStats(shortUrl, startTime, endTime);
    }

    @GetMapping("/timeseries")
    public List<TimeSeriesStatsDTO> getTimeSeriesStats(
            @RequestParam(required = false) String shortUrl,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestParam(defaultValue = "day") String type) {
        return statisticsService.getTimeSeriesStats(shortUrl, startTime, endTime, type);
    }

    @GetMapping("/locations")
    public List<LocationStatsDTO> getLocationStats(
            @RequestParam(required = false) String shortUrl,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestParam(defaultValue = "country") String type) {
        return statisticsService.getLocationStats(shortUrl, startTime, endTime, type);
    }

    @GetMapping("/metrics")
    public List<MetricCountStatsDTO> getMetricCountStats(
            @RequestParam(required = false) String shortUrl,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @Parameter(examples = {
                    @ExampleObject("referer_type"), @ExampleObject("referer"),
                    @ExampleObject("device_type"), @ExampleObject("device"),
                    @ExampleObject("os"), @ExampleObject("browser"),
                    @ExampleObject("language"), @ExampleObject("timezone")
            })
            @RequestParam String type) {
        return statisticsService.getMetricCountStats(shortUrl, startTime, endTime, type);
    }
}
