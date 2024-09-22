package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.api.dto.AuthCommand;
import com.github.xioshe.less.url.api.dto.AuthResponse;
import com.github.xioshe.less.url.api.dto.RefreshTokenCommand;
import com.github.xioshe.less.url.api.dto.SignupCommand;
import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.security.JwtTokenService;
import com.github.xioshe.less.url.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "认证")
@SecurityRequirements
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtTokenService jwtTokenService;

    @Operation(summary = "注册新用户", description = "创建一个新的用户并返回创建的用户信息")
    @ApiResponse(responseCode = "200", description = "注册成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @PostMapping("/signup")
    public User signup(@RequestBody SignupCommand command) {
        return authenticationService.signup(command);
    }

    @Operation(summary = "登录", description = "登录")
    @ApiResponse(responseCode = "200", description = "登录成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)))
    @PostMapping("/token")
    public AuthResponse login(@RequestBody AuthCommand command) {
        return authenticationService.login(command);
    }


    @Operation(summary = "刷新token", description = "刷新token")
    @ApiResponse(responseCode = "200", description = "刷新成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)))
    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenCommand command) {
        return authenticationService.refreshToken(command.getRefreshToken());
    }

    @Operation(summary = "登出", description = "登出")
    @ApiResponse(responseCode = "200", description = "登出成功")
    @PostMapping("/logout")
    public void logout(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        // "Bearer ".length() == 7
        String token = authHeader.substring(7);
        authenticationService.logout(token);
    }
}
