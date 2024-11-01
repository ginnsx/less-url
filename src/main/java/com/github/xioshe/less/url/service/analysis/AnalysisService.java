package com.github.xioshe.less.url.service.analysis;

import com.github.xioshe.less.url.entity.AccessRecord;
import com.github.xioshe.less.url.service.analysis.vo.AccessVO;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final UserAgentParser userAgentParser;
    private final RefererParser refererParser;
    private final IpGeoDetector ipGeoDetector;

    // UA 分析
    // Referer 分析
    // IP 分析
    // Language detect

    public AccessVO analyze(AccessRecord accessRecord) {
        try {
            var userAgentInfo = userAgentParser.parseUserAgent(accessRecord.getUserAgent());
            var refererInfo = refererParser.parseReferer(accessRecord.getReferer());
            var ipLocation = ipGeoDetector.detect(accessRecord.getIp());
            var language = detectLanguage(accessRecord.getLanguage());

            return AccessVO.builder()
                    .ip(accessRecord.getIp())
                    .userAgent(accessRecord.getUserAgent())
                    .shortUrl(accessRecord.getShortUrl())
                    .accessTime(accessRecord.getAccessTime())
                    .os(Strings.nullToEmpty(userAgentInfo.getOs()))
                    .brand(Strings.nullToEmpty(userAgentInfo.getBrand()))
                    .browser(Strings.nullToEmpty(userAgentInfo.getBrowser()))
                    .device(Strings.nullToEmpty(userAgentInfo.getDevice()))
                    .deviceType(Strings.nullToEmpty(userAgentInfo.getDeviceType()))
                    .continent(Strings.nullToEmpty(ipLocation.getContinent()))
                    .timezone(Strings.nullToEmpty(ipLocation.getTimezone()))
                    .country(Strings.nullToEmpty(ipLocation.getCountry()))
                    .region(Strings.nullToEmpty(ipLocation.getRegion()))
                    .city(Strings.nullToEmpty(ipLocation.getCity()))
                    .referer(Strings.nullToEmpty(refererInfo.getReferer()))
                    .refererType(Strings.nullToEmpty(refererInfo.getRefererType()))
                    .language(Strings.nullToEmpty(language))
                    .build();
        } catch (Exception e) {
            log.warn("Error parsing access record: {}", accessRecord, e);
        }

        return AccessVO.builder()
                .ip(accessRecord.getIp())
                .userAgent(accessRecord.getUserAgent())
                .shortUrl(accessRecord.getShortUrl())
                .accessTime(accessRecord.getAccessTime())
                .build();
    }


    public String detectLanguage(String acceptLanguage) {
        try {
            if (acceptLanguage != null && !acceptLanguage.isEmpty()) {
                // 解析 Accept-Language 头
                Locale locale = Locale.LanguageRange.parse(acceptLanguage)
                        .stream()
                        .findFirst()
                        .map(range -> Locale.forLanguageTag(range.getRange()))
                        .orElse(Locale.getDefault());

                // 返回语言代码（例如 "en", "zh", "es" 等）
                return locale.getDisplayLanguage();
            }
        } catch (Exception e) {
            log.warn("Error parsing Accept-Language header: {}", acceptLanguage, e);
        }

        // 如果没有 Accept-Language 头，返回默认语言
        return Locale.getDefault().getDisplayLanguage();
    }
}
