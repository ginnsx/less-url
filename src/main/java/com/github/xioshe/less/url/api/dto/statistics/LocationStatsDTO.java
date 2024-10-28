package com.github.xioshe.less.url.api.dto.statistics;

import lombok.Data;

@Data
public class LocationStatsDTO {
    private String country;
    private String region;
    private String city;
    private long count;
}