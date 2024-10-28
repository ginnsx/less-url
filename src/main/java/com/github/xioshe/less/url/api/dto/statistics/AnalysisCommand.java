package com.github.xioshe.less.url.api.dto.statistics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalysisCommand {

    private String shortUrl;
    private LocalDateTime startTime;
    @Schema(description = "end time, inclusive，until current hour")
    private LocalDateTime endTime;
    private Long userId;

    public String getOwnerId() {
        return userId == null ? null : "u_" + userId;
    }

}
