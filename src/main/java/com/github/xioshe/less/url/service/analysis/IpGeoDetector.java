package com.github.xioshe.less.url.service.analysis;

import com.github.xioshe.less.url.service.analysis.vo.IPLocationVO;

public interface IpGeoDetector {

    IPLocationVO detect(String ip);

}
