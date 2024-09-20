package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.api.dto.LoginCommand;
import com.github.xioshe.less.url.api.dto.LoginResponse;
import com.github.xioshe.less.url.api.dto.SignupCommand;
import com.github.xioshe.less.url.config.JwtTokenDecoder;
import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "认证")
@SecurityRequirements
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtTokenDecoder jwtTokenDecoder;

    @Operation(summary = "注册新用户", description = "创建一个新的用户并返回创建的用户信息")
    @ApiResponse(responseCode = "200", description = "注册成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @PostMapping("/signup")
    public User signup(@RequestBody SignupCommand command) {
        return authenticationService.signup(command);
    }

    @Operation(summary = "登录", description = "登录")
    @ApiResponse(responseCode = "200", description = "登录成功",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class)))
    @PostMapping("/token")
    public LoginResponse login(@RequestBody LoginCommand command) {
        User authentication = authenticationService.authenticate(command);
        String token = jwtTokenDecoder.generateToken(authentication);
        return new LoginResponse(token, jwtTokenDecoder.getExpirationTime());
    }
}
