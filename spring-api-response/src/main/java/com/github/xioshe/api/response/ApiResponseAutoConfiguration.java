package com.github.xioshe.api.response;


import com.github.xioshe.api.response.wrapped.ExceptionHandlers;
import com.github.xioshe.api.response.wrapped.WrappedResponseAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class ApiResponseAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WrappedResponseAdvice wrappedResponseAdvice() {
        return new WrappedResponseAdvice();
    }

    @Bean
    @ConditionalOnMissingBean
    public ExceptionHandlers exceptionHandlers() {
        return new ExceptionHandlers();
    }

}
