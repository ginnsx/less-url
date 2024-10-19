package com.github.xioshe.less.url.service;

import com.github.xioshe.less.url.util.constants.RedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitCountService {

    private final StringRedisTemplate redisTemplate;

    public void record(String shortUrl) {
        var key = RedisKeys.VISIT_COUNT_PREFIX + shortUrl;
//        redisTemplate.opsForHash().increment(key, "clicks", 1);
        redisTemplate.opsForValue().increment(key);
    }

    public long getVisitCount(String shortUrl) {
        var key = RedisKeys.VISIT_COUNT_PREFIX + shortUrl;
        String count = redisTemplate.opsForValue().get(key);
        return count != null ? Long.parseLong(count) : 0;
    }

    // 批量获取短链访问次数
    public Map<String, Long> batchGetVisitCounts(List<String> shortUrls) {
        List<String> keys = shortUrls.stream()
                .map(url -> RedisKeys.VISIT_COUNT_PREFIX + url)
                .collect(Collectors.toList());

        List<String> counts = redisTemplate.opsForValue().multiGet(keys);

        Map<String, Long> result = new HashMap<>();
        for (int i = 0; i < shortUrls.size(); i++) {
            String count = counts == null ? null : counts.get(i);
            result.put(shortUrls.get(i), count != null ? Long.parseLong(count) : 0);
        }

        return result;
    }
}
