package com.github.xioshe.less.url.api.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.xioshe.less.url.util.mybatis.QueryWrapperBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collection;

@Data
public class BaseQuery<T> {


    @Schema(description = "用户ID", example = "1001")
    private Long userId;

    @Schema(description = "记录ID", example = "2001")
    private Long id;

    @Schema(description = "ID列表，用于批量查询", example = "3001,4001")
    private Collection<Long> id_in;

    public QueryWrapper<T> toQueryWrapper() {
        return QueryWrapperBuilder.build(this);
    }
}
