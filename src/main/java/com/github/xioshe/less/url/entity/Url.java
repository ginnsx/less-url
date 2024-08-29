package com.github.xioshe.less.url.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Url {
    private Long id;

    /**
    * 短链接
    */
    private String shortUrl;

    /**
    * 原始链接
    */
    private String originalUrl;

    /**
    * 用户ID
    */
    private Long userId;

    /**
    * 状态
    */
    private Integer status;

    /**
    * 创建时间
    */
    private Date createTime;

    /**
    * 更新时间
    */
    private Date updateTime;
}