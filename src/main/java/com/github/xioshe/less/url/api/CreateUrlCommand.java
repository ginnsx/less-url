package com.github.xioshe.less.url.api;


import lombok.Data;

import java.util.Date;

@Data
public class CreateUrlCommand {

    private String originalUrl;
    private String customAlias;
    private Date expirationDate;

    private Long userId;
    //    private String apiDevKey;

}
