package com.github.xioshe.less.url.api;


import com.github.xioshe.less.url.api.dto.CreateUrlCommand;
import com.github.xioshe.less.url.service.AccessRecordService;
import com.github.xioshe.less.url.service.UrlService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "短链接")
@RequiredArgsConstructor
@RestController
@RequestMapping("urls")
public class UrlController {

    private final UrlService urlService;
    private final AccessRecordService accessRecordService;

    @Value("${less.url.host:http://localhost:8080/}")
    private String baseUrl;


    @Operation(summary = "生成短链接", description = "生成短链接")
    @ApiResponse(responseCode = "200", description = "短链接生成成功")
    @PostMapping
    public String shorten(@Parameter(description = "原始链接") @RequestBody @Validated CreateUrlCommand command) {
        return baseUrl + urlService.shorten(command);
    }

    @Hidden
    @DeleteMapping("/{shortUrl}")
    public void delete(@PathVariable String shortUrl) {
        // todo token 获取 apiDevKey
    }


    @Operation(summary = "获取短链接访问记录", description = "获取短链接访问记录")
    @ApiResponse(responseCode = "200", description = "短链接访问记录获取成功")
    @GetMapping("/{shortUrl}/access-records")
    public int accessRecords(@Parameter(description = "短链接") @PathVariable String shortUrl) {
        return accessRecordService.countByShortUrl(shortUrl);
    }

}
