package com.github.xioshe.less.url.entity.analysis;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("lu_dim_platform")
public class PlatformDim {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String os;
    private String browser;
}