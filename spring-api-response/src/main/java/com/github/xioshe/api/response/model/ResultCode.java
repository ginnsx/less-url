package com.github.xioshe.api.response.model;

public enum ResultCode {

    SUCCESS(1, "success"),
    PARAM_INVALID(1001, "invalid parameter"),
    USER_NOT_EXIST(2004, "user not exist");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
