package com.github.xioshe.less.url.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Deque;

import static org.assertj.core.api.Assertions.assertThat;

class RotatingSecretKeyManagerTest {

    private final RotatingSecretKeyManager keyManager = new RotatingSecretKeyManager();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(keyManager, "secret", "thisIsAVeryLongSecretKeyForTesting123");
    }

    @Test
    void rotation() {
        var currentKey = keyManager.getCurrentKey();
        keyManager.rotateKeys();
        assertThat(keyManager.getCurrentKey()).isNotSameAs(currentKey);
        assertThat(keyManager.secretKeys()).hasSize(2);
        assertThat(keyManager.secretKeys()).contains(currentKey);
    }

    @Test
    void afterPropertiesSet_shouldInitializeKeys() throws Exception {
        keyManager.afterPropertiesSet();

        Deque<SecretKey> keys = (Deque<SecretKey>) ReflectionTestUtils.getField(keyManager, "keys");
        Assertions.assertNotNull(keys);
        Assertions.assertEquals(1, keys.size());
    }

    @Test
    void getCurrentKey_shouldReturnLatestKey() throws Exception {
        keyManager.afterPropertiesSet();

        SecretKey key1 = keyManager.getCurrentKey();
        Assertions.assertNotNull(key1);

        keyManager.rotateKeys();
        SecretKey key2 = keyManager.getCurrentKey();
        Assertions.assertNotNull(key2);
        Assertions.assertNotEquals(key1, key2);
    }

    @Test
    void rotateKeys_shouldAddNewKeyAndRemoveOldIfExceedsMaxKeys() throws Exception {
        keyManager.afterPropertiesSet();

        keyManager.rotateKeys();
        keyManager.rotateKeys();

        Deque<SecretKey> keys = (Deque<SecretKey>) ReflectionTestUtils.getField(keyManager, "keys");
        Assertions.assertEquals(2, keys.size());

        SecretKey oldestKey = keys.getLast();
        keyManager.rotateKeys();

        Assertions.assertEquals(2, keys.size());
        Assertions.assertFalse(keys.contains(oldestKey));
    }

    @Test
    void secretKeys_shouldReturnAllKeys() throws Exception {
        keyManager.afterPropertiesSet();
        keyManager.rotateKeys();

        Assertions.assertEquals(2, ((Collection<SecretKey>) keyManager.secretKeys()).size());
    }

}