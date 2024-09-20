package com.github.xioshe.less.url.entity;

import com.github.xioshe.less.url.security.SecurityUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Schema(description = "用户")
@Data
public class User {
    private Long id;

    /**
     * 等级
     */
    private Byte level;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 密码
     */
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 删除标识，主键
     */
    private Long deletedKey;

    /**
     * 邮箱
     */
    private String email;

    /**
     * API 请求密钥
     */
    private String apiKey;

    /**
     * 创建时间
     */
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Date updateTime;

    public SecurityUser asSecurityUser() {
        return SecurityUser.builder()
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .nickname(this.username)
                .build();
    }

}