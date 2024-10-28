package com.github.xioshe.less.url.entity.auth;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Schema(description = "用户")
@Data
public class User implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
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
    @Size(min = 4, max = 16, message = "用户名长度为 4-16 位")
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

    @TableField(exist = false)
    @Schema(description = "角色")
    private Set<Role> roles = new HashSet<>();

    @Serial
    private static final long serialVersionUID = 1L;

    public void setRoles(Collection<Role> roles) {
        this.roles = new HashSet<>(roles);
    }
}