package com.github.xioshe.less.url.service;

import lombok.Getter;

@Getter
public enum VerificationType {
    REGISTER("register", "register-verification"),
    LOGIN("login", "login-verification"),
    RESET_PASSWORD("password", "reset-password");

    private final String value;
    private final String templateName;

    VerificationType(String value, String templateName) {
        this.value = value;
        this.templateName = templateName;
    }

}