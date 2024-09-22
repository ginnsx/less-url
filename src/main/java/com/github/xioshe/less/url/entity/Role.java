package com.github.xioshe.less.url.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Schema
@Data
public class Role implements Serializable {
    @Schema(description="")
    @NotNull(message = "不能为null")
    private Long id;

    /**
    * 角色名
    */
    @Schema(description="角色名")
    @Size(max = 20,message = "角色名最大长度要小于 20")
    @NotBlank(message = "角色名不能为空")
    private String roleName;

    /**
    * 创建时间
    */
    @Schema(description="创建时间")
    private Date createAt;

    /**
    * 更新时间
    */
    @Schema(description="更新时间")
    private Date updateAt;

    @Serial
    private static final long serialVersionUID = 1L;
}