package com.github.xioshe.less.url.service.analysis.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAgentVO {

    private String browser;
    private String browserVersion;
    private String os;
    private String osVersion;
    private String device;
    private String brand;
    private String deviceType;
    private boolean isBot;
    private String language;
    private String ua;
}
