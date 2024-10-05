package com.github.xioshe.less.url.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyCodeService {

    private final static String VERIFY_CODE_KEY_PREFIX = "verify:code:";

    private final StringRedisTemplate redisTemplate;

    @Getter
    @Setter
    private int expirationMinutes = 5;

    public String generate(String contacts, String type) {
        String code = generateCode();
        redisTemplate.opsForValue().set(getKey(contacts, type), code, expirationMinutes, TimeUnit.MINUTES);
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

    public boolean exists(String contacts, String type) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getKey(contacts, type)));
    }

    public boolean verify(String contacts, String type, String code) {
        if (code == null) {
            return false;
        }
        return code.equals(redisTemplate.opsForValue().get(getKey(contacts, type)));
    }

    private static String getKey(String contacts, String type) {
        return VERIFY_CODE_KEY_PREFIX + type + ":" + contacts;
    }
}
