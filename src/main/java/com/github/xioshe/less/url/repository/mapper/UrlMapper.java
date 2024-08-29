package com.github.xioshe.less.url.repository.mapper;

import com.github.xioshe.less.url.entity.Url;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface UrlMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Url record);

    int insertSelective(Url record);

    Url selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Url record);

    int updateByPrimaryKey(Url record);

    boolean existsByShortUrl(String shortUrl);

    String selectByOriginalUrlAndUserId(@Param("originalUrl") String originalUrl,
                                        @Param("userId") Long userId);

    Url selectByShortUrl(String shortUrl);
}