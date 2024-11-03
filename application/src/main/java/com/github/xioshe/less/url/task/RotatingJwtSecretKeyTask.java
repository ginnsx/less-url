package com.github.xioshe.less.url.task;

import com.github.xioshe.less.url.security.RotatingSecretKeyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RotatingJwtSecretKeyTask {

    private final RotatingSecretKeyManager secretKeyManager;

    @Scheduled(cron = "${security.jwt.key.rotation.cron:0 0 0 * * ?}")
    public void rotateKeys() {
        secretKeyManager.rotateKeys();
    }
}
