package com.github.xioshe.less.url.repository;

import com.github.xioshe.less.url.entity.EmailTemplate;
import com.github.xioshe.less.url.mapper.EmailTemplateMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class EmailTemplateRepository extends BaseRepository<EmailTemplateMapper, EmailTemplate> {

    @Cacheable(cacheNames = "emails", key = "#name")
    public EmailTemplate findByName(String name) {
        return lambdaQuery().eq(EmailTemplate::getName, name).one();
    }
}
