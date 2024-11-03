package com.github.xioshe.less.url.service.analysis;

import com.github.xioshe.less.url.service.analysis.vo.RefererVO;
import com.github.xioshe.less.url.util.constants.RefererType;
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class RefererParser {
    private static final Set<String> SEARCH_ENGINES = Sets.newHashSet("google", "bing", "yahoo", "baidu");
    private static final Set<String> SOCIAL_MEDIAS = Sets.newHashSet("facebook", "x", "linkedin",
            "instagram", "tiktok", "weibo");

    public RefererVO parseReferer(String referer) {
        RefererVO info = new RefererVO();

        if (referer == null || referer.isEmpty()) {
            info.setRefererType(RefererType.DIRECT);
            info.setReferer(RefererType.DIRECT);
            return info;
        }

        try {
            URL url = new URL(referer);
            String host = url.getHost();

            if (SEARCH_ENGINES.stream().anyMatch(host::contains)) {
                info.setRefererType(RefererType.SEARCH_ENGINE);
            } else if (SOCIAL_MEDIAS.stream().anyMatch(host::contains)) {
                info.setRefererType(RefererType.SOCIAL_MEDIA);
            } else {
                info.setRefererType(RefererType.OTHERS);
            }

            var refererValue = Stream.of(SEARCH_ENGINES, SOCIAL_MEDIAS).flatMap(Set::stream)
                    .filter(host::contains)
                    .map(s -> s + ".com")
                    .findFirst()
                    .orElse(host);
            info.setReferer(refererValue);
            info.setUrl(referer);

        } catch (Exception e) {
            info.setRefererType(RefererType.INVALID);
            info.setReferer(referer);
        }

        return info;
    }
}