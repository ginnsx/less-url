package com.github.xioshe.less.url.config;


import com.github.xioshe.less.url.service.analysis.UapUserAgentParser;
import com.github.xioshe.less.url.service.analysis.UserAgentParser;
import com.github.xioshe.less.url.service.analysis.YauaaUserAgentParser;
import com.github.xioshe.less.url.service.link.HashEncodingUrlShorter;
import com.github.xioshe.less.url.service.link.UrlShorter;
import com.github.xioshe.less.url.util.codec.Base58Codec;
import com.github.xioshe.less.url.util.codec.Encoder;
import com.github.xioshe.less.url.util.hash.HashFunctions;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import ua_parser.Parser;

import java.io.IOException;
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

    @Bean
    @ConditionalOnBean(UserAgentAnalyzer.class)
    @ConditionalOnMissingBean
    public UserAgentParser userAgentParser(UserAgentAnalyzer userAgentAnalyzer) {
        return new YauaaUserAgentParser(userAgentAnalyzer);
    }

    /**
     * UAP Parser
     */
    @Bean
    @ConditionalOnProperty(name = "lu.link.user-agent-parser", havingValue = "uap", matchIfMissing = true)
    public Parser uaParser() throws IOException {
        return new Parser();
    }

    @Bean
    @ConditionalOnProperty(name = "lu.link.user-agent-parser", havingValue = "uap", matchIfMissing = true)
    public UserAgentParser uapUserAgentParser(Parser parser) {
        return new UapUserAgentParser(parser);
    }

    /**
     * 分析 User-Agent，用于短链访问记录分析
     */
    @Bean
    @ConditionalOnProperty(name = "lu.link.user-agent-parser", havingValue = "yauaa")
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer.newBuilder()
                .hideMatcherLoadStats() // 减少创建实例时的日志
                .immediateInitialization() // 立即初始化，减少使用时的延迟，但需消耗更多内存和更多创建时的时间
                .withField(UserAgent.AGENT_NAME)
                .withField(UserAgent.AGENT_VERSION)
                .withField(UserAgent.OPERATING_SYSTEM_NAME)
                .withField(UserAgent.OPERATING_SYSTEM_VERSION)
                .withField(UserAgent.DEVICE_NAME)
                .withField(UserAgent.DEVICE_BRAND)
                .withField(UserAgent.DEVICE_CLASS)
                .withField(UserAgent.AGENT_CLASS)
                .build();
    }

    @Bean
    @ConditionalOnProperty(name = "lu.link.user-agent-parser", havingValue = "yauaa")
    public UserAgentParser yauaaUserAgentParser(UserAgentAnalyzer userAgentAnalyzer) {
        return new YauaaUserAgentParser(userAgentAnalyzer);
    }
}
