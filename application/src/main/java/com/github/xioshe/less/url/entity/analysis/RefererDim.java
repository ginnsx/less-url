package com.github.xioshe.less.url.entity.analysis;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("lu_dim_referer")
public class RefererDim {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String refererType;
    private String referer;
}