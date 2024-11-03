package com.github.xioshe.api.response;


import com.github.xioshe.api.response.wrapped.WrappedExceptionHandlers;
import com.github.xioshe.api.response.wrapped.WrappedResponseAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ApiResponseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WrappedResponseAdvice wrappedResponseAdvice() {
        return new WrappedResponseAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    public WrappedExceptionHandlers wrappedExceptionHandlers() {
        return new WrappedExceptionHandlers();
    }

}
