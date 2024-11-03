package com.github.xioshe.less.url.entity.analysis;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("lu_dim_geo")
public class GeoDim {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String country;
    private String region;
    private String city;
    private String continent;
}