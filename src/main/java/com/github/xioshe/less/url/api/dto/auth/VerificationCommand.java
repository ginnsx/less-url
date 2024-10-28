package com.github.xioshe.less.url.api.dto.auth;

import com.github.xioshe.less.url.service.auth.VerificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "发送验证码请求")
@Data
public class VerificationCommand {

    @Schema(description = "邮箱地址")
    @NotNull
    @Email
    private String email;

    @Schema(description = "业务类型")
    @NotNull
    private VerificationType type;
}
