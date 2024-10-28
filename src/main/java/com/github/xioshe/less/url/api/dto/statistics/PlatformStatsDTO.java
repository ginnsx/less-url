package com.github.xioshe.less.url.api.dto.statistics;

import lombok.Data;

@Data
public class PlatformStatsDTO {
    private String os;
    private String browser;
    private long clicks;
}
