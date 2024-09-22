package com.github.xioshe.less.url.entity;

import com.github.xioshe.less.url.security.CustomGrantedAuthority;
import com.github.xioshe.less.url.security.SecurityUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Schema(description = "用户")
@Data
public class User implements Serializable {

    private Long id;

    /**
     * 等级
     */
    @Schema(description = "等级")
    private Byte level;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @Size(max = 20, message = "用户名最大长度要小于 20")
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 积分
     */
    @Schema(description = "积分")
    private Integer integral;

    /**
     * 余额
     */
    @Schema(description = "余额")
    private BigDecimal balance;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @Size(max = 255, message = "密码最大长度要小于 255")
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 状态
     */
    @Schema(description = "状态")
    private Byte status;

    /**
     * 删除标识，主键
     */
    @Schema(description = "删除标识，主键")
    private Long deletedKey;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @Size(max = 255, message = "邮箱最大长度要小于 255")
    private String email;

    /**
     * API 请求密钥
     */
    @Schema(description = "API 请求密钥")
    @Size(max = 64, message = "API 请求密钥最大长度要小于 64")
    private String apiKey;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "角色")
    private List<Role> roles;

    @Serial
    private static final long serialVersionUID = 1L;

    public SecurityUser asSecurityUser() {
        Set<CustomGrantedAuthority> authorities = CollectionUtils.isEmpty(roles)
                ? new HashSet<>()
                : roles.stream()
                .map(role -> new CustomGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toSet());
        return SecurityUser.builder()
                .email(this.email)
                .username(this.username)
                .password(this.password)
                .nickname(this.username)
                .authorities(authorities)
                .build();
    }
}