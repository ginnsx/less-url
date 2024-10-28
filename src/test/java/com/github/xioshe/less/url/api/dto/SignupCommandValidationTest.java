package com.github.xioshe.less.url.api.dto;

import com.github.xioshe.less.url.api.dto.auth.SignupCommand;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SignupCommandValidationTest {

    private final Validator validator;

    SignupCommandValidationTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void Password() {
        var command = new SignupCommand();
        command.setVerifyCode("123456");
        command.setEmail("test@example.com");
        command.setUsername("test");

        command.setPassword("123456Ab+=>?.");
        assertThat(validator.validate(command)).isEmpty();
    }

    @Test
    void Password_not_null() {
        var command = new SignupCommand();
        command.setVerifyCode("123456");
        command.setEmail("test@example.com");
        command.setUsername("test");

        assertThat(validator.validate(command))
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("密码不能为空");
    }

    @Test
    void VerifyCode_only_digits() {
        var command = new SignupCommand();
        command.setEmail("test@example.com");
        command.setUsername("test");
        command.setPassword("123456Ab+=>?.");

        command.setVerifyCode("12345a");
        assertThat(validator.validate(command))
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("验证码必须为 6 位数字");

        command.setVerifyCode("123456");
        assertThat(validator.validate(command)).isEmpty();
    }
}