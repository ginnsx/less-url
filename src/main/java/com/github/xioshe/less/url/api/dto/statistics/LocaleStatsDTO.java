package com.github.xioshe.less.url.api.dto.statistics;

import lombok.Data;

@Data
public class LocaleStatsDTO {
    private String language;
    private String timezone;
    private long clicks;
}