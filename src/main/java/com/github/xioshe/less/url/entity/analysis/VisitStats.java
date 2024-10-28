package com.github.xioshe.less.url.entity.analysis;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitStats {
    private Long id;
    private String shortUrl;
    private LocalDateTime visitTime;
    private Long geoId;
    private Long deviceId;
    private Long platformId;
    private Long localeId;
    private Long refererId;
    private Integer clicks;
    private Integer visitors;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}