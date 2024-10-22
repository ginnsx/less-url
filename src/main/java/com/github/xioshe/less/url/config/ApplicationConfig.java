package com.github.xioshe.less.url.config;


import com.github.xioshe.less.url.shorter.HashEncodingUrlShorter;
import com.github.xioshe.less.url.shorter.UrlShorter;
import com.github.xioshe.less.url.util.codec.Base58Codec;
import com.github.xioshe.less.url.util.codec.Encoder;
import com.github.xioshe.less.url.util.hash.HashFunctions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;


@EnableAsync
@EnableScheduling
@EnableConfigurationProperties(AppProperties.class)
@Configuration
public class ApplicationConfig {

    @Bean
    public UrlShorter urlShorter() {
        Encoder encoder = new Base58Codec();
        return new HashEncodingUrlShorter(encoder, HashFunctions.MURMUR3);
    }

    /**
     * 全局时钟，便于单元测试
     *
     * @return Clock
     */
    @Bean
    public Clock globalClock() {
        return Clock.systemDefaultZone();
    }
}
