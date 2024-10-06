package com.github.xioshe.less.url.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;


@Schema(description = "短链接生成请求")
@Data
public class CreateLinkCommand {

    @NotNull
    @URL
    private String originalUrl;

    @Schema(description = "自定义短链接由字母、数字、下划线组成，最长6位", example = "abc")
    @Pattern(regexp = "^[a-zA-Z0-9]{0,6}$", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String customAlias;

    @Schema(description = "短链接过期时间", example = "2024-09-08 00:00:00")
    @Future
    private LocalDateTime expirationTime;
    //    private String apiDevKey;

}
