package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.service.auth.GuestIdService;
import com.github.xioshe.less.url.util.constants.RedisKeys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GuestIdServiceTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private GuestIdService guestIdService;

    @Test
    void generateGuestId_shouldReturnUniqueId() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        String guestId = guestIdService.generateGuestId();

        assertNotNull(guestId);
        verify(valueOperations).set(eq(RedisKeys.GUEST_ID_PREFIX + guestId), anyString(), eq(14L), eq(TimeUnit.DAYS));
    }

    @Test
    void isValidGuestId_shouldReturnTrueForValidId() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        String guestId = "valid-guest-id";
        when(redisTemplate.hasKey(RedisKeys.GUEST_ID_PREFIX + guestId)).thenReturn(true);

        boolean isValid = guestIdService.isValidGuestId(guestId);

        assertTrue(isValid);
        verify(valueOperations).set(eq(RedisKeys.GUEST_ID_PREFIX + guestId), anyString(), eq(14L), eq(TimeUnit.DAYS));
    }

    @Test
    void isValidGuestId_shouldReturnFalseForInvalidId() {
        String guestId = "invalid-guest-id";
        when(redisTemplate.hasKey(RedisKeys.GUEST_ID_PREFIX + guestId)).thenReturn(false);

        boolean isValid = guestIdService.isValidGuestId(guestId);

        assertFalse(isValid);
        verify(valueOperations, never()).set(anyString(), anyString(), anyLong(), any(TimeUnit.class));
    }
}