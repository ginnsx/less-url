package com.github.xioshe.less.url.service.statistics;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitCountService {

    // 记录访问次数获取频率不高，没必要使用 Redis 存储
    // 先从数据库事实表获取，出现性能问题再考虑添加计数器优化
}
