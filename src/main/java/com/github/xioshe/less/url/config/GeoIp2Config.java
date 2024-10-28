package com.github.xioshe.less.url.config;

import com.github.xioshe.less.url.service.analysis.GeoIp2IpGeoDetector;
import com.github.xioshe.less.url.service.analysis.IpGeoDetector;
import com.maxmind.geoip2.DatabaseReader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

@Configuration
@ConditionalOnProperty(prefix = "lu.ip-geo", name = "provider", havingValue = "geoip2")
@RequiredArgsConstructor
public class GeoIp2Config {

    private final AppProperties appProperties;

    @Bean(destroyMethod = "close")
    public DatabaseReader databaseReader() throws IOException {
        String path = appProperties.getIpGeo().getDatabasePath();
        File dbFile = ResourceUtils.getFile(path);
        return new DatabaseReader.Builder(dbFile).build();
    }

    @Bean
    @ConditionalOnBean(name = "databaseReader")
    public IpGeoDetector ipGeoDetector() throws IOException {
        return new GeoIp2IpGeoDetector(databaseReader());
    }
}