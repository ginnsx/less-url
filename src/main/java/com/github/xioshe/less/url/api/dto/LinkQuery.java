package com.github.xioshe.less.url.api.dto;


import com.github.xioshe.less.url.api.dto.common.BaseQuery;
import com.github.xioshe.less.url.entity.Link;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springdoc.core.annotations.ParameterObject;

import java.util.Date;

@ParameterObject
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkQuery extends BaseQuery<Link> {

    @Schema(hidden = true, description = "精确匹配用户 ID")
    private String ownerId;

    @Schema(description = "精确匹配短链接")
    private String shortUrl_eq;

    @Schema(description = "模糊匹配短链接")
    private String shortUrl_like;

    @Schema(description = "左侧模糊匹配短链接")
    private String shortUrl_likeLeft;

    @Schema(description = "右侧模糊匹配短链接")
    private String shortUrl_likeRight;

    @Schema(description = "精确匹配长链接")
    private String longUrl_eq;

    @Schema(description = "模糊匹配长链接")
    private String longUrl_like;

    @Schema(description = "左侧模糊匹配长链接")
    private String longUrl_likeLeft;

    @Schema(description = "右侧模糊匹配长链接")
    private String longUrl_likeRight;

    @Schema(description = "精确匹配点击次数")
    private Integer visits_eq;

    @Schema(description = "点击次数大于")
    private Integer visits_gt;

    @Schema(description = "点击次数小于")
    private Integer visits_lt;

    @Schema(description = "点击次数大于等于")
    private Integer visits_ge;

    @Schema(description = "点击次数小于等于")
    private Integer visits_le;

    @Schema(description = "精确匹配过期时间")
    private Date expiresAt_eq;

    @Schema(description = "过期时间晚于")
    private Date expiresAt_gt;

    @Schema(description = "过期时间早于")
    private Date expiresAt_lt;

    @Schema(description = "过期时间不早于")
    private Date expiresAt_ge;

    @Schema(description = "过期时间不晚于")
    private Date expiresAt_le;

    @Schema(description = "是否为自定义短链接")
    private Boolean isCustom_eq;

    @Schema(description = "精确匹配创建时间")
    private Date createdAt_eq;

    @Schema(description = "创建时间晚于")
    private Date createdAt_gt;

    @Schema(description = "创建时间早于")
    private Date createdAt_lt;

    @Schema(description = "创建时间不早于")
    private Date createdAt_ge;

    @Schema(description = "创建时间不晚于")
    private Date createdAt_le;

    @Schema(description = "精确匹配更新时间")
    private Date updatedAt_eq;

    @Schema(description = "更新时间晚于")
    private Date updatedAt_gt;

    @Schema(description = "更新时间早于")
    private Date updatedAt_lt;

    @Schema(description = "更新时间不早于")
    private Date updatedAt_ge;

    @Schema(description = "更新时间不晚于")
    private Date updatedAt_le;

}