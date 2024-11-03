package com.github.xioshe.less.url.service.auth;

import com.github.xioshe.less.url.util.constants.RedisKeys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationService {

    private final StringRedisTemplate redisTemplate;

    private final Environment env;

    @Getter
    @Setter
    private int expirationMinutes = 5;

    public String generate(String type, String identity) {
        String code = generateCode();
        redisTemplate.opsForValue().set(getKey(type, identity), code, expirationMinutes, TimeUnit.MINUTES);
        return code;
    }


    /**
     * 生成随机六位验证码
     *
     * @return 验证码
     */
    private String generateCode() {
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100_000));
    }

    public boolean verify(String type, String identity, String code) {
        if (!isProd()) return true;
        if (code == null) {
            return false;
        }
        return code.equals(redisTemplate.opsForValue().get(getKey(type, identity)));
    }

    private boolean isProd() {
        return Arrays.stream(env.getActiveProfiles())
                .anyMatch("prod"::equalsIgnoreCase);
    }

    private static String getKey(String type, String identity) {
        return RedisKeys.VERIFY_CODE_KEY_PREFIX + type + ":" + identity;
    }

    @Async
    public void clean(String type, String identity) {
        redisTemplate.delete(getKey(type, identity));
    }
}
