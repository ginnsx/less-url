package com.github.xioshe.less.url.entity.analysis;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("lu_dim_locale")
public class LocaleDim {
    private Long id;
    private String timezone;
    private String language;
}