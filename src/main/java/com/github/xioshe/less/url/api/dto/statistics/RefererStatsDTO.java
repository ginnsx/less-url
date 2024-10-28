package com.github.xioshe.less.url.api.dto.statistics;

import lombok.Data;

@Data
public class RefererStatsDTO {
    private String refererType;
    private String referer;
    private long clicks;
}