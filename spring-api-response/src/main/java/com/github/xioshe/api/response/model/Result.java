package com.github.xioshe.api.response.model;

import java.io.Serializable;

public class Result implements Serializable {

    private final Integer code;
    private final String msg;
    private final Object data;

    public Result(ResultCode resultCode, Object data) {
        this.code = resultCode.getCode();
        this.msg = resultCode.getMessage();
        this.data = data;
    }

    public Result(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public static Result success(Object data) {
        return new Result(ResultCode.SUCCESS, data);
    }

    public static Result failure(ResultCode resultCode) {
        return new Result(resultCode, null);
    }

    public static Result failure(Integer code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result failure(Integer code, String message, Object data) {
        return new Result(code, message, data);
    }
}
