package com.github.xioshe.less.url.service.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.InputStreamSource;

@Data
@AllArgsConstructor
public class AttachmentVO {
    private String name;
    private InputStreamSource file;
}