package com.github.xioshe.less.url.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordStrengthCheckerTest {
    private String weakPassword;
    private String strongPassword;

    @BeforeEach
    public void setUp() {
        weakPassword = "weak";
        strongPassword = "StrongPassword123!";
    }

    @Test
    public void checkStrength_WeakPassword_ReturnsLowStrength() {
        int strength = PasswordStrengthChecker.checkStrength(weakPassword);
        assertEquals(1, strength, "Expected strength 1 for a weak password");
    }

    @Test
    public void checkStrength_StrongPassword_ReturnsHighStrength() {
        int strength = PasswordStrengthChecker.checkStrength(strongPassword);
        assertEquals(6, strength, "Expected strength 6 for a strong password");
    }

    @Test
    public void checkStrength_EmptyPassword_ReturnsZeroStrength() {
        int strength = PasswordStrengthChecker.checkStrength("");
        assertEquals(0, strength, "Expected strength 0 for an empty password");
    }

    @Test
    public void checkStrength_PasswordWithOnlyUppercase_ReturnsPartialStrength() {
        int strength = PasswordStrengthChecker.checkStrength("ALLUPPER");
        assertEquals(2, strength, "Expected strength 2 for a password with only uppercase letters");
    }

    @Test
    public void checkStrength_PasswordWithOnlyLowercase_ReturnsPartialStrength() {
        int strength = PasswordStrengthChecker.checkStrength("alllower");
        assertEquals(2, strength, "Expected strength 2 for a password with only lowercase letters");
    }

    @Test
    public void checkStrength_PasswordWithOnlyNumbers_ReturnsPartialStrength() {
        int strength = PasswordStrengthChecker.checkStrength("12345678");
        assertEquals(2, strength, "Expected strength 2 for a password with only numbers");
    }

    @Test
    public void checkStrength_PasswordWithOnlySpecialChars_ReturnsPartialStrength() {
        int strength = PasswordStrengthChecker.checkStrength("!!!###");
        assertEquals(1, strength, "Expected strength 1 for a password with only special characters");
    }
}