package com.github.xioshe.less.url.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

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