package com.github.xioshe.less.url.service;


import com.github.xioshe.less.url.entity.AccessRecord;
import com.github.xioshe.less.url.repository.AccessRecordRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessRecordService {

    private final AccessRecordRepository accessRecordRepository;

    public void save(AccessRecord accessRecord) {
        accessRecordRepository.insert(accessRecord);
    }

    public int countByShortUrl(String shortUrl) {
        return accessRecordRepository.countByShortUrl(shortUrl);
    }

    public void save(String url, HttpServletRequest request) {
        AccessRecord accessRecord = new AccessRecord();
        accessRecord.setShortUrl(url);
        accessRecord.setUserAgent(request.getHeader("User-Agent"));
        accessRecord.setIp(request.getRemoteAddr());
        accessRecord.setReferer(request.getHeader("referer"));
        accessRecord.setAccessTime(new java.util.Date());
        accessRecordRepository.insert(accessRecord);
    }
}
