package com.github.xioshe.less.url.api.dto.statistics;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimeSeriesStatsDTO {
    private LocalDateTime time;
    private long visits;
    private long visitors;
}