package com.github.xioshe.less.url.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RefreshTokenCommand {

    @NotBlank
    private String refreshToken;

}
