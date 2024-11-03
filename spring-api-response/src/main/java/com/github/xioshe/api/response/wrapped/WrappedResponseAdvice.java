package com.github.xioshe.api.response.wrapped;

import com.github.xioshe.api.response.model.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class WrappedResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 忽略 Result 类型的方法
        var returnClazz = returnType.getParameterType();
        if (Result.class.isAssignableFrom(returnClazz)) {
            return false;
        }

        return returnType.hasMethodAnnotation(WrappedResponse.class)
               || returnType.getContainingClass().isAnnotationPresent(WrappedResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 将 body 包装为 Result0
        return Result.success(body);
    }
}
