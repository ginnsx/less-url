package com.github.xioshe.less.url.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Schema(description = "登录响应")
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthResponse {

    @Schema(description = "token，短令牌")
    private final String accessToken;

    @Schema(description = "token 类型，默认为 Bearer")
    @Builder.Default
    private final String tokenType = "Bearer";

    @Schema(description = "token 过期时间，单位秒")
    private final long expiresIn;

    @Schema(description = "刷新 token，有效期更长的长令牌")
    private String refreshToken;

    @Schema(description = "token 对应的权限范围")
    private String scope;

}
