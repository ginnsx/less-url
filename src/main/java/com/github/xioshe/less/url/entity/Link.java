package com.github.xioshe.less.url.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Link implements Serializable {
    private Long id;

    /**
    * 短链接
    */
    @Schema(description = "短链接")
    private String shortUrl;

    /**
    * 原始链接
    */
    @Schema(description = "原始链接")
    private String originalUrl;

    /**
    * 用户ID
    */
    @Schema(description = "用户ID")
    private String ownerId;

    /**
    * 状态
    */
    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "是否自定义短链接")
    private Boolean isCustom;

    /**
     * 过期时间
     */
    @Schema(description = "过期时间")
    private LocalDateTime expiresAt;

    /**
     * 访问次数
     */
    private Integer clicks = 0;

    /**
     * 最后访问时间
     */
    private LocalDateTime lastAccessTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(description = "版本号")
    @Version
    private Integer version;

    @Serial
    private static final long serialVersionUID = 1L;

    public Link addUrlPrefix(String urlPrefix) {
        if (shortUrl != null && !shortUrl.startsWith(urlPrefix)) {
            shortUrl = urlPrefix + shortUrl;
        }
        return this;
    }
}