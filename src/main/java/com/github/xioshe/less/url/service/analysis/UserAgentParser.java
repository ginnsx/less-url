package com.github.xioshe.less.url.service.analysis;

import com.github.xioshe.less.url.service.analysis.vo.UserAgentVO;

public interface UserAgentParser {

    UserAgentVO parseUserAgent(String userAgent);
}
