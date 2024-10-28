package com.github.xioshe.less.url.api.dto.auth;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;



@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "登录响应")
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthResponse {

    @JsonProperty("access_token")
    @Schema(description = "token，短令牌")
    private final String accessToken;

    @JsonProperty("token_type")
    @Schema(description = "token 类型，默认为 Bearer")
    @Builder.Default
    private final String tokenType = "Bearer";

    @JsonProperty("expires_in")
    @Schema(description = "token 过期时间，单位秒")
    private final long expiresIn;

    @JsonProperty("refresh_token")
    @Schema(description = "刷新 token，有效期更长的长令牌")
    private String refreshToken;

    @Schema(description = "token 对应的权限范围")
    private String[] scope;

}
