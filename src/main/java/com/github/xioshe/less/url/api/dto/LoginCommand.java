package com.github.xioshe.less.url.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Schema(description = "登录请求")
@RequiredArgsConstructor
public class LoginCommand {
    // todo valid
    private final String username;
    private final String password;
}
