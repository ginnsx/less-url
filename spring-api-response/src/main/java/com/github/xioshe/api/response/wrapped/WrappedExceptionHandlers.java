package com.github.xioshe.api.response.wrapped;


import com.github.xioshe.api.response.exception.AutoWrappedException;
import com.github.xioshe.api.response.model.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WrappedExceptionHandlers {

    @ExceptionHandler(AutoWrappedException.class)
    public Result handleAutoWrappedException(AutoWrappedException e) {
        return Result.failure(e.getCode(), e.getMessage(), e.getData());
    }

    @ExceptionHandler(Throwable.class)
    public Result handleException(Throwable e) {
        var msg = e.getMessage();
        if (msg == null) {
            msg = "未知异常";
        }
        return Result.failure(5000, msg);
    }

}
