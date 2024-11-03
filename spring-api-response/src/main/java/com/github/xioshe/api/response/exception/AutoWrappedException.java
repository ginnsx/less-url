package com.github.xioshe.api.response.exception;

import com.github.xioshe.api.response.model.ResultCode;

public class AutoWrappedException extends RuntimeException {

    private final Integer code;
    private Object data;

    public AutoWrappedException(ResultCode result) {
        super(result.getMessage());
        this.code = result.getCode();
    }

    public AutoWrappedException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public AutoWrappedException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public AutoWrappedException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public AutoWrappedException(Integer code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.data = data;
    }

    public AutoWrappedException(Integer code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public AutoWrappedException(Integer code, Object data, Throwable cause) {
        super(cause);
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
