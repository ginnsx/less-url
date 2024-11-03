package com.github.xioshe.less.url.service.analysis;

import com.github.xioshe.less.url.service.analysis.vo.IPLocationVO;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@RequiredArgsConstructor
public class GeoIp2IpGeoDetector implements IpGeoDetector {

    private static final String CHINA = "中国";
    private static final String ZH_CN = "zh-CN";

    private final DatabaseReader databaseReader;

    @Override
    public IPLocationVO detectPublicAddress(String ip) {
        log.debug("analysis ip location: {}", ip);
        CityResponse response;
        try {
            var inetAddress = InetAddress.getByName(ip);
            response = databaseReader.city(inetAddress);
        } catch (UnknownHostException e) {
            log.warn("unknown host: {}", ip);
            return IPLocationVO.builder().ip(ip).build();
        } catch (IOException | GeoIp2Exception e) {
            log.warn("analysis ip location failed: {}", ip, e);
            return IPLocationVO.builder().ip(ip).build();
        }

        var continent = response.getContinent().getName();
        var country = response.getCountry().getName();
        var timezone = response.getLocation().getTimeZone();
        // 国内城市显示中文名，国外城市显示英文名
        var region = response.getMostSpecificSubdivision().getName();
        var city = response.getCity().getName();

        return IPLocationVO.builder()
                .ip(ip)
                .city(city)
                .region(region)
                .country(country)
                .continent(continent)
                .timezone(timezone)
                .build();
    }
}
