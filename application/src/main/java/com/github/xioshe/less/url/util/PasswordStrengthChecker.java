package com.github.xioshe.less.url.util;

import com.github.xioshe.less.url.util.constants.RegexPatterns;

import java.util.Arrays;
import java.util.regex.Pattern;

public class PasswordStrengthChecker {

    /**
     * Check the strength of a password based on certain criteria.
     *
     * @param password The password to check.
     * @return An integer value indicating the strength of the password.
     *         The higher the value, the stronger the password.
     *         0–100
     */
    public static int checkStrength(String password) {
        boolean hasLowerCase = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasUpperCase = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasNumber = Pattern.compile("\\d").matcher(password).find();
        boolean hasSpecialChar = Pattern.compile(RegexPatterns.SPECIAL_CHARS_PATTERN).matcher(password).find();
        int length = password.length();

        int strength = 0;

        // 基础分数：长度
        if (length >= 8) strength += 10;
        if (length >= 12) strength += 10;
        if (length >= 16) strength += 10;
        if (length >= 20) strength += 10;

        // 字符类型
        Boolean[] types = {hasLowerCase, hasUpperCase, hasNumber, hasSpecialChar};
        long typeCount = Arrays.stream(types).filter(Boolean::booleanValue).count();
        strength += (int) (typeCount * 10);

        // 额外奖励
        if (typeCount >= 3 && length >= 8) strength += 10;
        if (typeCount == 4 && length >= 12) strength += 10;
        if (typeCount == 4 && length >= 20) strength += 10;

        // 惩罚：如果密码只包含字母或数字，降低分数
        if (typeCount == 1 && (hasLowerCase || hasUpperCase || hasNumber)) {
            strength = Math.max(0, strength - 20);
        }

        // 确保分数在0-100之间
        return Math.min(Math.max(strength, 0), 100);
    }
}
