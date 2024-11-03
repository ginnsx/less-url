package com.github.xioshe.less.url.entity.analysis;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("lu_dim_device")
public class DeviceDim {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String device;
    private String brand;
    private String deviceType;
}