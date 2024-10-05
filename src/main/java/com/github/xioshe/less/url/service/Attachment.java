package com.github.xioshe.less.url.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.InputStreamSource;

@Data
@AllArgsConstructor
public class Attachment {
    private String name;
    private InputStreamSource file;
}