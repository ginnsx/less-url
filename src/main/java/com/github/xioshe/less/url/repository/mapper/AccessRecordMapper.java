package com.github.xioshe.less.url.repository.mapper;

import com.github.xioshe.less.url.entity.AccessRecord;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AccessRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AccessRecord record);

    int insertSelective(AccessRecord record);

    AccessRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AccessRecord record);

    int updateByPrimaryKey(AccessRecord record);

    int countByShortUrl(String shortUrl);
}