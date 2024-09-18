package com.github.xioshe.less.url.api.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LoginCommand {
    // todo valid
    private final String username;
    private final String password;
}
