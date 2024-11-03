package com.github.xioshe.less.url.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterEmailCommand {

    @Schema(description = "邮箱地址")
    @NotNull
    @Email
    private String email;

    @Schema(description = "验证码")
    @Digits(integer = 6, fraction = 0, message = "验证码必须为 6 位数字")
    private String verifyCode;
}
