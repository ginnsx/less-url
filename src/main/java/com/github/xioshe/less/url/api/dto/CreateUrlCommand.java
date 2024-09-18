package com.github.xioshe.less.url.api.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.Date;

@Data
public class CreateUrlCommand {

    @URL
    private String originalUrl;
    @Pattern(regexp = "^[a-zA-Z0-9]{0,6}$", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String customAlias;
    @Future
    private Date expirationTime;

    @Positive
    @NotNull
    private Long userId;
    //    private String apiDevKey;

}
