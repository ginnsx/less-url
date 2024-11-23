package com.github.xioshe.less.url.exception;

import lombok.Getter;

@Getter
public class CustomAliasDuplicatedException extends RuntimeException {
    public static final String MESSAGE = "自定义短链接已存在";
    public static final String CODE = "custom_alias_duplicated";
    private final String customAlias;

    public CustomAliasDuplicatedException(String customAlias) {
        super(MESSAGE);
        this.customAlias = customAlias;
    }

    public String getCode() {
        return CODE;
    }

}
