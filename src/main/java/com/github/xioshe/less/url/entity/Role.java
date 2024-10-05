package com.github.xioshe.less.url.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Schema
@Data
public class Role implements Serializable {
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
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @Schema(description = "版本号")
    @Version
    private Integer version;

    /**
     * 权限列表
     */
    @TableField(exist = false)
    @Schema(description = "权限列表")
    private Set<Permission> permissions;

    @Serial
    private static final long serialVersionUID = 1L;

    public void setPermissions(Collection<Permission> permissions) {
        this.permissions = Set.copyOf(permissions);
    }
}