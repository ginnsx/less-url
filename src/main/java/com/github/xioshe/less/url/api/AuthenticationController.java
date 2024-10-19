package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.api.dto.AuthCommand;
import com.github.xioshe.less.url.api.dto.AuthResponse;
import com.github.xioshe.less.url.api.dto.GuestIdResponse;
import com.github.xioshe.less.url.api.dto.LoginCommand;
import com.github.xioshe.less.url.api.dto.RefreshTokenCommand;
import com.github.xioshe.less.url.api.dto.RegisterEmailCommand;
import com.github.xioshe.less.url.api.dto.SignupCommand;
import com.github.xioshe.less.url.api.dto.VerificationCommand;
import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "认证")
@SecurityRequirements // Swagger 不需要添加认证信息
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "验证注册邮箱", description = "验证注册邮箱，发送验证码")
    @PostMapping("/register-verification")
    public void registerVerification(@RequestBody @Validated RegisterEmailCommand command) {
        authenticationService.verifyEmailBeforeRegister(command);
    }

    @Operation(summary = "发送邮件验证码", description = "发送邮件验证码")
    @PostMapping("/email-verification")
    public void sendEmailVerification(@RequestBody @Validated VerificationCommand command) {
        authenticationService.sendEmailVerification(command);
    }

    @Operation(summary = "注册新用户", description = "创建一个新的用户并返回创建的用户信息")
    @ApiResponse(responseCode = "200", description = "注册成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @PostMapping("/register")
    public User signup(@RequestBody @Validated SignupCommand command) {
        return authenticationService.signup(command);
    }

    @Operation(summary = "用户登录", description = "账户密码登录或者验证码登录")
    @ApiResponse(responseCode = "200", description = "登录成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)))
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginCommand command) {
        return authenticationService.login(command);
    }

    @Operation(summary = "获取 token", description = " 获取 token")
    @ApiResponse(responseCode = "200", description = "认证成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)))
    @PostMapping("/token")
    public AuthResponse auth(@RequestBody @Validated AuthCommand command) {
        return authenticationService.auth(command);
    }

    @Operation(summary = "刷新 token", description = "刷新 token")
    @ApiResponse(responseCode = "200", description = "刷新成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)))
    @PostMapping("/refresh")
    public AuthResponse refreshToken(@RequestBody RefreshTokenCommand command) {
        return authenticationService.refreshToken(command.getRefreshToken(), command.getAccessToken());
    }

    @Operation(summary = "获取游客 id", description = "获取游客 id")
    @ApiResponse(responseCode = "200", description = "获取成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GuestIdResponse.class)))
    @GetMapping("/guest-id")
    public GuestIdResponse generateGuestId() {
        return authenticationService.generateGuestId();
    }
}
