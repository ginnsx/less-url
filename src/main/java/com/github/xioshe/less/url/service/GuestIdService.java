package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.util.constants.RedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GuestIdService {

    private static final long GUEST_ID_EXPIRATION = 14; // 14 days

    private final StringRedisTemplate redisTemplate;

    public String generateGuestId() {
        String guestId = UUID.randomUUID().toString();
        setGuestIdWithExpiration(guestId);
        return guestId;
    }

    public boolean isValidGuestId(String guestId) {
        Boolean exists = redisTemplate.hasKey(RedisKeys.GUEST_ID_PREFIX + guestId);
        if (Boolean.TRUE.equals(exists)) {
            // 如果 guest id 存在，刷新其过期时间
            setGuestIdWithExpiration(guestId);
            return true;
        }
        return false;
    }

    private void setGuestIdWithExpiration(String guestId) {
        String key = RedisKeys.GUEST_ID_PREFIX + guestId;
        Long ttl = redisTemplate.getExpire(key, TimeUnit.DAYS);
        // 距离上次更新超过 1 天才刷新，避免频繁更新 Redis 影响性能
        if (ttl == null || ttl < GUEST_ID_EXPIRATION - 1) {
            redisTemplate.opsForValue().set(
                    RedisKeys.GUEST_ID_PREFIX + guestId,
                    String.valueOf(System.currentTimeMillis()),
                    GUEST_ID_EXPIRATION,
                    TimeUnit.DAYS
            );
        }
    }

    /**
     * 获取 guest id 的最后活跃时间
     */
    public Long getLastActiveTime(String guestId) {
        String timestamp = redisTemplate.opsForValue().get(RedisKeys.GUEST_ID_PREFIX + guestId);
        return timestamp != null ? Long.parseLong(timestamp) : null;
    }
}