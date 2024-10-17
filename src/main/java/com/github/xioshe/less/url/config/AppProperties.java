package com.github.xioshe.less.url.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("lu")
public class AppProperties {

    @Setter
    private String baseUrl;

    private final LinkConfig link = new LinkConfig();

    @Getter
    @Setter
    public static class LinkConfig {
        /**
         * 短链接最大长度
         */
        private int maxLength = 6;
        /**
         * 短链接默认有效期，单位秒
         */
        private int defaultTimeToLiveSeconds = 86400; // P1D
    }
}
