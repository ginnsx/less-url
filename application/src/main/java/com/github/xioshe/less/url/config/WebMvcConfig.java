package com.github.xioshe.less.url.config;

import com.github.xioshe.less.url.api.Api;
import com.github.xioshe.less.url.api.interceptor.LinkRedirectInterceptor;
import com.github.xioshe.less.url.service.link.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LinkService linkService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(linkRedirectInterceptor())
                .order(0) // 设置最高优先级
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns(
                        "/",
                        "/api/**",
                        "/static/**",
                        "/assets/**",
                        "/favicon.ico",
                        "/actuator/**",
                        "/doc.html",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/error"  // 排除错误页面
                );
    }

    @Bean
    public LinkRedirectInterceptor linkRedirectInterceptor() {
        return new LinkRedirectInterceptor(linkService);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", c -> c.isAnnotationPresent(Api.class));
    }

    /**
     * 处理请求参数中的 long 类型时间戳，转换为 Date 和 LocalDateTime  等类型
     *
     * @param registry FormatterRegistry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new LongStringToDate());
		registry.addConverter(new LongStringToLocalDateTime());
		registry.addConverter(new LongStringToLocalDate());
		registry.addConverter(new LongStringToLocalTime());
    }

    private static class LongStringToDate implements Converter<String, Date> {
        @Override
        public Date convert(String source) {
            try {
                long timestamp = Long.parseLong(source);
                return new Date(timestamp);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

	private static class LongStringToLocalDateTime implements Converter<String, LocalDateTime> {

        @Override
        public LocalDateTime convert(String source) {
            try {
                long timestamp = Long.parseLong(source);
                return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

	private static class LongStringToLocalDate implements Converter<String, LocalDate> {

        @Override
        public LocalDate convert(String source) {
            try {
                long timestamp = Long.parseLong(source);
                return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

	private static class LongStringToLocalTime implements Converter<String, LocalTime> {

		@Override
		public LocalTime convert(String source) {
			try {
				long timestamp = Long.parseLong(source);
				return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalTime();
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}

}