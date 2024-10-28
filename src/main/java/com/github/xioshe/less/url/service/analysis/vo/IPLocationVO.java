package com.github.xioshe.less.url.service.analysis.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IPLocationVO {
    private String ip;
    private String country;
    private String region;
    private String city;
    private String continent;
    private String timezone;
}