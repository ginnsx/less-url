package com.github.xioshe.less.url.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@Schema(description = "登录响应")
@RequiredArgsConstructor
public class LoginResponse {

    private final String token;
    @Schema(description = "token 过期时间")
    private final long expireTime;

}
