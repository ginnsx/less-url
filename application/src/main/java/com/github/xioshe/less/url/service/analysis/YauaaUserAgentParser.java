package com.github.xioshe.less.url.service.analysis;

import com.github.xioshe.less.url.service.analysis.vo.UserAgentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

@Slf4j
@RequiredArgsConstructor
public class YauaaUserAgentParser implements UserAgentParser {

    private final UserAgentAnalyzer userAgentAnalyzer;

    @Override
    public UserAgentVO parseUserAgent(String userAgent) {
        try {
            var ua = userAgentAnalyzer.parse(userAgent);
            return UserAgentVO.builder()
                    .browser(ua.getValue(UserAgent.AGENT_NAME).trim())
                    .browserVersion(ua.getValue(UserAgent.AGENT_VERSION).trim())
                    .os(ua.getValue(UserAgent.OPERATING_SYSTEM_NAME).trim())
                    .osVersion(ua.getValue(UserAgent.OPERATING_SYSTEM_VERSION).trim())
                    .device(ua.getValue(UserAgent.DEVICE_NAME).trim())
                    .brand(ua.getValue(UserAgent.DEVICE_BRAND).trim())
                    .deviceType(ua.getValue(UserAgent.DEVICE_CLASS).trim())
                    .isBot("browser".equalsIgnoreCase(ua.getValue(UserAgent.AGENT_CLASS).trim()))
                    .ua(userAgent)
                    .build();
        } catch (Exception e) {
            log.warn("Error parsing user agent: {}", userAgent, e);
            return UserAgentVO.builder().ua(userAgent).build();
        }
    }
}
