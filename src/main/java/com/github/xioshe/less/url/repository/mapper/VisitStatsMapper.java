package com.github.xioshe.less.url.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.xioshe.less.url.api.dto.statistics.BasicStatsDTO;
import com.github.xioshe.less.url.api.dto.statistics.LocationStatsDTO;
import com.github.xioshe.less.url.api.dto.statistics.MetricCountStatsDTO;
import com.github.xioshe.less.url.api.dto.statistics.TimeSeriesStatsDTO;
import com.github.xioshe.less.url.entity.analysis.VisitStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VisitStatsMapper extends BaseMapper<VisitStats> {
    BasicStatsDTO getBasicStats(@Param("shortUrl") String shortUrl,
                                @Param("startTime") LocalDateTime startTime,
                                @Param("endTime") LocalDateTime endTime);

    List<TimeSeriesStatsDTO> getTimeSeriesStats(@Param("shortUrl") String shortUrl,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime,
                                                @Param("interval") String interval);

    List<LocationStatsDTO> getCountryStats(@Param("shortUrl") String shortUrl,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    List<LocationStatsDTO> getRegionStats(@Param("shortUrl") String shortUrl,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    List<LocationStatsDTO> getCityStats(@Param("shortUrl") String shortUrl,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);

    List<MetricCountStatsDTO> getRefererStats(@Param("shortUrl") String shortUrl,
                                              @Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime,
                                              @Param("type") String type);

    List<MetricCountStatsDTO> getLocaleStats(@Param("shortUrl") String shortUrl,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime,
                                             @Param("type") String type);

    List<MetricCountStatsDTO> getDeviceStats(@Param("shortUrl") String shortUrl,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime,
                                             @Param("type") String type);

    List<MetricCountStatsDTO> getPlatformStats(@Param("shortUrl") String shortUrl,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime,
                                               @Param("type") String type);
}