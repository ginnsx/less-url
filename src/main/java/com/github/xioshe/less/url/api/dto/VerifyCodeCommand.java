package com.github.xioshe.less.url.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Schema(description = "发送验证码请求")
@Data
public class VerifyCodeCommand {

    @Schema(description = "邮箱地址")
    @Email
    private String email;

}
