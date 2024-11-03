package com.github.xioshe.less.url.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordStrengthCheckerTest {
    @Test
    void testWeakPassword() {
        int strength = PasswordStrengthChecker.checkStrength("weak");
        assertThat(strength).isEqualTo(0);
    }

    @Test
    void testStrongPassword() {
        int strength = PasswordStrengthChecker.checkStrength("Str0ngP@ssw0rd");
        assertThat(strength).isEqualTo(80);
    }

    @Test
    void testVeryStrongPassword() {
        int strength = PasswordStrengthChecker.checkStrength("Str0ngP@ssw0rd!!!2024");
        assertThat(strength).isEqualTo(100);
    }

    @Test
    void testPasswordWithOnlyLowerCase() {
        int strength = PasswordStrengthChecker.checkStrength("onlylowercase");
        assertThat(strength).isEqualTo(10);
    }

    @ParameterizedTest
    @CsvSource({
            "password123, 30",
            "Password123, 50",
            "P@ssw0rd, 60",
            "L0ngP@ssw0rdWithManyChars, 100"
    })
    void testVariousPasswords(String password, int expectedStrength) {
        int strength = PasswordStrengthChecker.checkStrength(password);
        assertThat(strength).isEqualTo(expectedStrength);
    }

    @Test
    void testPasswordLength() {
        assertThat(PasswordStrengthChecker.checkStrength("Aa1!")).isLessThan(
                PasswordStrengthChecker.checkStrength("Aa1!5678")
        );
        assertThat(PasswordStrengthChecker.checkStrength("Aa1!5678")).isLessThan(
                PasswordStrengthChecker.checkStrength("Aa1!567890123456")
        );
        assertThat(PasswordStrengthChecker.checkStrength("Aa1!567890123456")).isLessThan(
                PasswordStrengthChecker.checkStrength("Aa1!5678901234567890")
        );
    }

    @Test
    void testCharacterTypes() {
        assertThat(PasswordStrengthChecker.checkStrength("lowercase")).isLessThan(
                PasswordStrengthChecker.checkStrength("Lowercase")
        );
        assertThat(PasswordStrengthChecker.checkStrength("Lowercase")).isLessThan(
                PasswordStrengthChecker.checkStrength("Lowercase123")
        );
        assertThat(PasswordStrengthChecker.checkStrength("Lowercase123")).isLessThan(
                PasswordStrengthChecker.checkStrength("Lowercase123!")
        );
    }
}