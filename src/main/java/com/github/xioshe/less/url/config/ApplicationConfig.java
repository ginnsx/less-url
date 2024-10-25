package com.github.xioshe.less.url.config;


import com.github.xioshe.less.url.shorter.HashEncodingUrlShorter;
import com.github.xioshe.less.url.shorter.UrlShorter;
import com.github.xioshe.less.url.util.codec.Base58Codec;
import com.github.xioshe.less.url.util.codec.Encoder;
import com.github.xioshe.less.url.util.hash.HashFunctions;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
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

    /**
     * 短链接生成器
     */
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

    /**
     * 分析 User-Agent，用于短链访问记录分析
     */
    @Bean
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer.newBuilder()
                .hideMatcherLoadStats() // 减少创建实例时的日志
                .immediateInitialization() // 立即初始化，减少使用时的延迟，但需消耗更多内存和更多创建时的时间
                .build();
    }

    public static void main(String[] args) {
        var ua = "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.120 Mobile Safari/537.36";
        var uaa = new ApplicationConfig().userAgentAnalyzer();
        var result = uaa.parse(ua);

        for (String fieldName: result.getAvailableFieldNamesSorted()) {
            System.out.println(fieldName + " = " + result.getValue(fieldName));
        }
        System.out.println();
        for (String fieldName: result.getCleanedAvailableFieldNamesSorted()) {
            System.out.println(fieldName + " = " + result.getValue(fieldName));
        }
    }
}
