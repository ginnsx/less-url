package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.api.dto.CreateUrlCommand;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CreateUrlCommandValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void OriginalUrl() {
        var command = new CreateUrlCommand();
        command.setUserId(1L);
        command.setOriginalUrl("https://www.google.com");
        assertThat(validator.validate(command)).isEmpty();
        command.setOriginalUrl("http://www.google.com");
        assertThat(validator.validate(command)).isEmpty();
        command.setOriginalUrl("not a url");
        assertThat(validator.validate(command)).hasSize(1);
    }

    @Test
    void CustomAlias_shorter_than_six() {
        var command = new CreateUrlCommand();
        command.setUserId(1L);
        command.setOriginalUrl("https://www.google.com");
        command.setCustomAlias("123456");
        assertThat(validator.validate(command)).isEmpty();
        command.setCustomAlias("1234567");
        assertThat(validator.validate(command)).hasSize(1);
    }

    @Test
    void CustomAlias_contains_letters_or_digits() {
        var command = new CreateUrlCommand();
        command.setUserId(1L);
        command.setOriginalUrl("https://www.google.com");
        command.setCustomAlias("123AbC");
        assertThat(validator.validate(command)).isEmpty();
        command.setCustomAlias("123_");
        assertThat(validator.validate(command)).hasSize(1);
    }

    @Test
    void UserId_not_null() {
        var command = new CreateUrlCommand();
        command.setOriginalUrl("https://www.google.com");
        assertThat(validator.validate(command)).hasSize(1);
    }

    @Test
    void UserId_bigger_than_zero() {
        var command = new CreateUrlCommand();
        command.setOriginalUrl("https://www.google.com");
        command.setUserId(0L);
        assertThat(validator.validate(command)).hasSize(1);
    }

    @Test
    void ExpirationTime() {
        var command = new CreateUrlCommand();
        command.setUserId(1L);
        command.setOriginalUrl("https://www.google.com");

        var now = LocalDateTime.now();
        command.setExpirationTime(now.plusSeconds(1));
        assertThat(validator.validate(command)).isEmpty();

        command.setExpirationTime(now);
        assertThat(validator.validate(command)).hasSize(1);
    }


}