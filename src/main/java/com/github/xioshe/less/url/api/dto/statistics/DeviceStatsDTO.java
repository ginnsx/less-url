package com.github.xioshe.less.url.api.dto.statistics;

import lombok.Data;

@Data
public class DeviceStatsDTO {
    private String device;
    private String deviceType;
    private long clicks;
}
