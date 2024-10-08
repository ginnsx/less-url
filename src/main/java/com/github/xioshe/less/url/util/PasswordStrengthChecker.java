package com.github.xioshe.less.url.util;

import java.util.regex.Pattern;

public class PasswordStrengthChecker {

    /**
     * Check the strength of a password based on certain criteria.
     *
     * @param password The password to check.
     * @return An integer value indicating the strength of the password.
     *         The higher the value, the stronger the password.
     *         0–6
     */
    public static int checkStrength(String password) {
        int strength = 0;

        // Check length
        if (password.length() >= 8) strength++;
        if (password.length() >= 12) strength++;

        // Check for uppercase letters
        if (Pattern.compile("[A-Z]").matcher(password).find()) strength++;

        // Check for lowercase letters
        if (Pattern.compile("[a-z]").matcher(password).find()) strength++;

        // Check for numbers
        if (Pattern.compile("\\d").matcher(password).find()) strength++;

        // Check for special characters
        if (Pattern.compile("[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]").matcher(password).find()) strength++;

        return strength;
    }
}
