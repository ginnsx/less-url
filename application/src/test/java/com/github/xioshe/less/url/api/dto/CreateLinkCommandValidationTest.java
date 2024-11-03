package com.github.xioshe.less.url.api.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CreateLinkCommandValidationTest {

    private final Validator validator;

    CreateLinkCommandValidationTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void OriginalUrl() {
        var command = new CreateLinkCommand();
        command.setOriginalUrl("https://www.google.com");
        assertThat(validator.validate(command)).isEmpty();
        command.setOriginalUrl("http://www.google.com");
        assertThat(validator.validate(command)).isEmpty();
        command.setOriginalUrl("not a url");
        assertThat(validator.validate(command)).hasSize(1);
    }

    @Test
    void CustomAlias_shorter_than_six() {
        var command = new CreateLinkCommand();
        command.setOriginalUrl("https://www.google.com");
        command.setCustomAlias("123456");
        assertThat(validator.validate(command)).isEmpty();
        command.setCustomAlias("1234567");
        assertThat(validator.validate(command)).hasSize(1);
    }

    @Test
    void CustomAlias_contains_letters_or_digits() {
        var command = new CreateLinkCommand();
        command.setOriginalUrl("https://www.google.com");
        command.setCustomAlias("123AbC");
        assertThat(validator.validate(command)).isEmpty();
        command.setCustomAlias("123_");
        assertThat(validator.validate(command)).hasSize(1);
    }

    @Test
    void expiresAt() {
        var command = new CreateLinkCommand();
        command.setOriginalUrl("https://www.google.com");

        var now = LocalDateTime.now();
        command.setExpiresAt(now.plusSeconds(1));
        assertThat(validator.validate(command)).isEmpty();

        command.setExpiresAt(now);
        assertThat(validator.validate(command)).hasSize(1);
    }


}