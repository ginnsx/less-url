package com.github.xioshe.less.url.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RotatingSecretKeyManagerTest {

    private final RotatingSecretKeyManager keyManager = new RotatingSecretKeyManager();

    @Test
    void rotation() {
        var currentKey = keyManager.getCurrentKey();
        keyManager.rotateKeys();
        assertThat(keyManager.getCurrentKey()).isNotSameAs(currentKey);
        assertThat(keyManager.secretKeys()).hasSize(2);
        assertThat(keyManager.secretKeys()).contains(currentKey);
    }

}