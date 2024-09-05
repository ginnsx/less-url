package com.github.xioshe.less.url.api;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.Date;

@Data
public class CreateUrlCommand {

    @URL
    private String originalUrl;
    @Pattern(regexp = "^[a-zA-Z0-9]{0,6}$", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String customAlias;
    private Date expirationDate;

    @Min(1)
    @NotNull
    private Long userId;
    //    private String apiDevKey;

}
