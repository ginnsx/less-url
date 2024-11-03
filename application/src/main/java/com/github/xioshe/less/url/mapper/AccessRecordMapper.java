package com.github.xioshe.less.url.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.xioshe.less.url.entity.AccessRecord;
import com.github.xioshe.less.url.entity.Link;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface AccessRecordMapper extends BaseMapper<AccessRecord> {

    List<Link> selectCountByShortUrls(List<String> shortUrls);

    List<Link> countByAccessTime(@Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    List<AccessRecord> selectAccessRecordsBetween(@Param("ownerId") String ownerId,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);
}