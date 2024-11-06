package com.github.xioshe.less.url.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties("lu")
public class AppProperties {

    private String shortUrlDomain;

    public String getShortUrlPrefix() {
        return shortUrlDomain.endsWith("/") ? shortUrlDomain : shortUrlDomain + "/";
    }

    @NestedConfigurationProperty
    private final LinkConfig link = new LinkConfig();

    @NestedConfigurationProperty
    private final IpGeoConfig ipGeo = new IpGeoConfig();

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
        /**
         * User-Agent 解析器类型：uap 或 yauaa
         */
        private String userAgentParser = "uap";
    }

    @Getter
    @Setter
    public static class IpGeoConfig {
        /**
         * IP 地理位置解析方式
         */
        private String provider = "geoip2";
        /**
         * GeoIP2 数据库文件路径
         */
        private String databasePath = "classpath:geo/GeoLite2-City.mmdb";
    }
}
