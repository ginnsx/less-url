package com.github.xioshe.less.url.api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenCommand {

    @JsonAlias("refresh_token")
    @NotBlank
    private String refreshToken;

    @JsonAlias("access_token")
    @NotBlank
    private String accessToken;

}
