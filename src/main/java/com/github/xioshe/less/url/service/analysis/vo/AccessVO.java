package com.github.xioshe.less.url.service.analysis.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessVO {
    private String ip;
    private String userAgent;

    private String shortUrl;
    private LocalDateTime accessTime;

    private String country;
    private String region;
    private String city;
    private String continent;

    private String device;
    private String brand;
    private String deviceType;

    private String os;
    private String browser;

    private String timezone;
    private String language;

    private String refererType;
    private String referer;
}
