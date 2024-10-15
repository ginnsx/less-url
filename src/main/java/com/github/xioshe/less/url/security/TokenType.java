package com.github.xioshe.less.url.security;

public enum TokenType {
    ACCESS,
    REFRESH;

    public String value() {
        return name().toLowerCase();
    }

    public static TokenType fromValue(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
