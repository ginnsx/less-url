package com.github.xioshe.less.url.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.xioshe.less.url.entity.Link;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LinkMapper extends BaseMapper<Link> {
    int updateVisitCount(Link link);
}