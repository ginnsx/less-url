package com.github.xioshe.less.url.api.dto.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema
@NoArgsConstructor
@AllArgsConstructor
public class AuthCommand {

    @Schema(description = "邮箱")
    @Email
    @NotNull
    private String email;

    @Schema(description = "密码")
    private String password;

}
