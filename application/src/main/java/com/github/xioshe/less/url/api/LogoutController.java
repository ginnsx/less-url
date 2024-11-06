package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.api.dto.auth.RefreshTokenCommand;
import com.github.xioshe.less.url.service.auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "登出")
@Api // api
@RequiredArgsConstructor
public class LogoutController {

    private final AuthenticationService authenticationService;


    @Operation(summary = "登出", description = "登出")
    @ApiResponse(responseCode = "200", description = "登出成功")
    @PostMapping("v1/logout")
    public void logout(@Parameter(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
                       @Parameter(hidden = true) @RequestBody RefreshTokenCommand command) {
        // "Bearer ".length() == 7
        String token = authHeader.substring(7);
        authenticationService.logout(token, command.getRefreshToken());
    }
}
