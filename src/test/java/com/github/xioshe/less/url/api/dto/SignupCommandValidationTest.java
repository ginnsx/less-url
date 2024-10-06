package com.github.xioshe.less.url.api.dto;

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
    void Password_max_length() {
        var command = new SignupCommand();
        command.setVerifyCode("123456");
        command.setEmail("test@example.com");
        command.setUsername("test");

        command.setPassword("aB123456789012345");
        assertThat(validator.validate(command))
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("密码由大小写字母、数字和特殊符号组成，长度为 8-16 位");
    }

    @Test
    void Password_min_length() {
        var command = new SignupCommand();
        command.setVerifyCode("123456");
        command.setEmail("test@example.com");
        command.setUsername("test");

        command.setPassword("123456A");
        assertThat(validator.validate(command))
                .singleElement()
                .extracting(ConstraintViolation::getMessage)
                .isEqualTo("密码由大小写字母、数字和特殊符号组成，长度为 8-16 位");
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