package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.api.dto.LoginCommand;
import com.github.xioshe.less.url.api.dto.LoginResponse;
import com.github.xioshe.less.url.api.dto.SignupCommand;
import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.service.AuthenticationService;
import com.github.xioshe.less.url.config.JwtTokenDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtTokenDecoder jwtTokenDecoder;

    @PostMapping("/signup")
    public User signup(@RequestBody SignupCommand command) {
        return authenticationService.signup(command);
    }

    @PostMapping("/token")
    public LoginResponse login(@RequestBody LoginCommand command) {
        User authentication = authenticationService.authenticate(command);
        String token = jwtTokenDecoder.generateToken(authentication);
        return new LoginResponse(token, jwtTokenDecoder.getExpirationTime());
    }
}
