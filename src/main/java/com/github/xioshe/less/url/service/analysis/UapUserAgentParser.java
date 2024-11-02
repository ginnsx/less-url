package com.github.xioshe.less.url.service.analysis;

import com.github.xioshe.less.url.service.analysis.vo.UserAgentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

@Slf4j
@RequiredArgsConstructor
public class UapUserAgentParser implements UserAgentParser {

    private final Parser uaParser;

    @Override
    public UserAgentVO parseUserAgent(String userAgent) {
        try {
            Client client = uaParser.parse(userAgent);

            return UserAgentVO.builder()
                    .browser(client.userAgent.family)
                    .browserVersion(formatVersion(client.userAgent))
                    .os(client.os.family)
                    .osVersion(formatVersion(client.os))
                    .device(client.device.family)
                    // UAP 没有这些信息，设置默认值
                    .brand("")
                    .deviceType(getDeviceType(client))
                    .isBot(isBot(client.userAgent.family))
                    .ua(userAgent)
                    .build();
        } catch (Exception e) {
            log.warn("Error parsing user agent: {}", userAgent, e);
            return UserAgentVO.builder().ua(userAgent).build();
        }
    }

    private String formatVersion(ua_parser.UserAgent ua) {
        if (ua.major == null) return "";
        StringBuilder version = new StringBuilder(ua.major);
        if (ua.minor != null) {
            version.append(".").append(ua.minor);
            if (ua.patch != null) {
                version.append(".").append(ua.patch);
            }
        }
        return version.toString();
    }

    private String formatVersion(ua_parser.OS os) {
        if (os.major == null) return "";
        StringBuilder version = new StringBuilder(os.major);
        if (os.minor != null) {
            version.append(".").append(os.minor);
            if (os.patch != null) {
                version.append(".").append(os.patch);
                if (os.patchMinor != null) {
                    version.append(".").append(os.patchMinor);
                }
            }
        }
        return version.toString();
    }

    private String getDeviceType(Client client) {
        String device = client.device.family.toLowerCase();
        if (device.contains("mobile") || device.contains("phone")) {
            return "Phone";
        } else if (device.contains("tablet")) {
            return "Tablet";
        } else if (device.equals("other")) {
            return "Desktop";
        }
        return device;
    }

    private boolean isBot(String userAgentFamily) {
        String family = userAgentFamily.toLowerCase();
        return family.contains("bot") ||
               family.contains("crawler") ||
               family.contains("spider");
    }
}