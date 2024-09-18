package com.github.xioshe.less.url.api.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class LoginResponse {

    private final String token;
    private final long expireTime;

}
