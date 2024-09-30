package com.github.xioshe.less.url.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Schema
@Data
public class Role implements Serializable {
    @Schema(description="")
    @NotNull(message = "不能为null")
    private Long id;

    @Schema(description="角色编码")
    @Size(max = 20,message = "角色编码最大长度要小于 20")
    @NotBlank(message = "角色编码不能为空")
    private String code;

    /**
    * 角色名
    */
    @Schema(description="角色名")
    @Size(max = 20,message = "角色名最大长度要小于 20")
    @NotBlank(message = "角色名不能为空")
    private String name;

    /**
     * 角色描述
     */
    @Schema(description = "角色描述")
    @Size(max = 255, message = "角色描述最大长度要小于 255")
    private String description;

    @Schema(description="是否启用")
    private Boolean enabled;

    /**
     * 权限列表
     */
    @Schema(description = "权限列表")
    private Set<Permission> permissions;

    /**
    * 创建时间
    */
    @Schema(description="创建时间")
    private Date createTime;

    /**
    * 更新时间
    */
    @Schema(description="更新时间")
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}