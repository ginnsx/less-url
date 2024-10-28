package com.github.xioshe.less.url.entity.auth;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Schema
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable {

    private Long id;

    /**
     * 权限编码
     */
    @Schema(description = "权限编码")
    @Size(max = 20, message = "权限编码长度不应该超过 20")
    @NotBlank(message = "权限编码不能为空")
    private String code;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称")
    @Size(max = 20, message = "权限名称长度不应该超过 20")
    @NotBlank(message = "权限名称不能为空")
    private String name;

    /**
     * 权限描述
     */
    @Schema(description = "权限描述")
    @Size(max = 255, message = "权限描述长度不应该超过 255")
    private String description;

    /**
     * 是否启用
     */
    @Schema(description = "是否启用")
    private Boolean enabled;

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

    public Permission(Long id, String code, String name, String description, Boolean enabled) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
    }
}