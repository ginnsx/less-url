package com.github.xioshe.less.url.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // deserializers
            builder.deserializers(new LocalDateDeserializer());
            builder.deserializers(new LocalDateTimeDeserializer());
            builder.deserializers(new LocalTimeDeserializer());

            // serializers
            builder.serializers(new LocalDateSerializer());
            builder.serializers(new LocalDateTimeSerializer());
            builder.serializers(new LocalTimeSerializer());
        };
    }

    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

        /**
         * 如果没有重写 handledType() 方法，会报错
         *
         * @return LocalDateTime.class
         */
        @Override
        public Class<LocalDateTime> handledType() {
            return LocalDateTime.class;
        }

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                long epochMilli = value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                gen.writeNumber(epochMilli);
            }
        }
    }

    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

        @Override
        public Class<?> handledType() {
            return LocalDateTime.class;
        }

        @Override
        public LocalDateTime deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
            try {
                long timestamp = Long.parseLong(parser.getValueAsString());
                // ts 负值也有意义，表示 1970 年之前的时间
                return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    public static class LocalDateSerializer extends JsonSerializer<LocalDate> {

        @Override
        public Class<LocalDate> handledType() {
            return LocalDate.class;
        }

        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                long epochMilli = value.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
                gen.writeNumber(epochMilli);
            }
        }
    }

    public static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

        @Override
        public Class<?> handledType() {
            return LocalDate.class;
        }

        @Override
        public LocalDate deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
            try {
                long timestamp = Long.parseLong(parser.getValueAsString());
                return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    public static class LocalTimeSerializer extends JsonSerializer<LocalTime> {

        @Override
        public Class<LocalTime> handledType() {
            return LocalTime.class;
        }

        @Override
        public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                long epochMilli = value.atDate(LocalDate.now())
                        .atZone(ZoneId.systemDefault())
                        .toInstant().toEpochMilli();
                gen.writeNumber(epochMilli);
            }
        }
    }

    public static class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

        @Override
        public Class<?> handledType() {
            return LocalTime.class;
        }

        @Override
        public LocalTime deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
            try {
                long timestamp = Long.parseLong(parser.getValueAsString());
                return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalTime();
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }
}
