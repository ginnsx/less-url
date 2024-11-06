package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.api.dto.auth.AuthCommand;
import com.github.xioshe.less.url.api.dto.auth.AuthResponse;
import com.github.xioshe.less.url.api.dto.auth.CheckEmailCommand;
import com.github.xioshe.less.url.api.dto.auth.CheckEmailResponse;
import com.github.xioshe.less.url.api.dto.auth.GuestIdResponse;
import com.github.xioshe.less.url.api.dto.auth.LoginCommand;
import com.github.xioshe.less.url.api.dto.auth.RefreshTokenCommand;
import com.github.xioshe.less.url.api.dto.auth.RegisterEmailCommand;
import com.github.xioshe.less.url.api.dto.auth.SignupCommand;
import com.github.xioshe.less.url.api.dto.auth.VerificationCommand;
import com.github.xioshe.less.url.entity.auth.User;
import com.github.xioshe.less.url.repository.auth.UserRepository;
import com.github.xioshe.less.url.service.auth.AuthenticationService;
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


@Tag(name = "认证")
@SecurityRequirements // Swagger 不需要添加认证信息
@Api
@RequestMapping("/v1/auth") // api/v1/auth
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Operation(summary = "检查邮箱", description = "检查邮箱是否已经注册")
    @PostMapping("/check-email")
    public CheckEmailResponse checkEmail(@RequestBody @Validated CheckEmailCommand command) {
        return new CheckEmailResponse(userRepository.existsByEmail(command.getEmail()));
    }

    @Operation(summary = "发送注册验证码", description = "验证注册邮箱，发送验证码")
    @PostMapping("/register-verification")
    public void registerVerification(@RequestBody @Validated RegisterEmailCommand command) {
        authenticationService.sendRegisterVerificationEmail(command);
    }

    @Operation(summary = "验证注册邮箱", description = "验证注册邮箱")
    @PostMapping("/register-verification/verify")
    public void verifyEmailBeforeRegister(@RequestBody @Validated RegisterEmailCommand command) {
        authenticationService.verifyEmailBeforeRegister(command);
    }

    @Operation(summary = "发送邮件验证码", description = "发送邮件验证码")
    @PostMapping("/email-verification")
    public void sendEmailVerification(@RequestBody @Validated VerificationCommand command) {
        authenticationService.sendEmailVerification(command);
    }

    @Operation(summary = "注册新用户", description = "创建新用户")
    @ApiResponse(responseCode = "200", description = "注册成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @PostMapping("/register")
    public void signup(@RequestBody @Validated SignupCommand command) {
        authenticationService.signup(command);
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
