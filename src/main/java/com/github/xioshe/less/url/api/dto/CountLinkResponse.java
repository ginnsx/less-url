package com.github.xioshe.less.url.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountLinkResponse {
    private int links;
    private long analytics;

    public CountLinkResponse(int links) {
        this.links = links;
    }
}
