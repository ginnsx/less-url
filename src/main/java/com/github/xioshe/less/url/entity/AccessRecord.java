package com.github.xioshe.less.url.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AccessRecord {
    private Long id;

    /**
     * 短链接
     */
    private String shortUrl;

    /**
     * 浏览器信息
     */
    private String userAgent;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 来源页面
     */
    private String referer;

    /**
     * 创建时间
     */
    private LocalDateTime accessTime;
}