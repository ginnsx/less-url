package com.github.xioshe.less.url.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Schema
@Data
public class EmailTemplate implements Serializable {

    private Long id;

    /**
     * 模板名称
     */
    @Schema(description = "模板名称")
    @Size(max = 50, message = "模板名称最大长度要小于 50")
    @NotBlank(message = "模板名称不能为空")
    private String name;

    /**
     * 邮件主题
     */
    @Schema(description = "邮件主题")
    @NotNull(message = "邮件主题不能为空")
    @Size(max = 255, message = "邮件主题最大长度要小于 255")
    private String subject;

    /**
     * 邮件内容
     */
    @Schema(description = "邮件内容")
    @NotNull(message = "邮件内容不能为空")
    private String content;

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
}
