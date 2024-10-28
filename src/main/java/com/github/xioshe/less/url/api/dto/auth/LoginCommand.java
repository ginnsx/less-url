package com.github.xioshe.less.url.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema
@Data
public class LoginCommand {

    @Schema(description = "邮箱")
    @Email
    @NotNull
    private String email;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "验证码")
    @Digits(integer = 6, fraction = 0, message = "验证码必须为 6 位数字")
    private String verifyCode;

    public boolean hasPassword() {
        return password != null && !password.isEmpty();
    }

    public boolean hasVerifyCode() {
        return verifyCode != null && !verifyCode.isEmpty();
    }

    @Schema(hidden = true)
    public boolean isValid() {
        return hasPassword() || hasVerifyCode();
    }
}
