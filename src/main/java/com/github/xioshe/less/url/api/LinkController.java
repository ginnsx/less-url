package com.github.xioshe.less.url.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xioshe.less.url.api.dto.CreateLinkCommand;
import com.github.xioshe.less.url.api.dto.LinkQuery;
import com.github.xioshe.less.url.api.dto.Pagination;
import com.github.xioshe.less.url.entity.Link;
import com.github.xioshe.less.url.repository.AccessRecordRepository;
import com.github.xioshe.less.url.security.SecurityUserHelper;
import com.github.xioshe.less.url.service.LinkService;
import com.github.xioshe.less.url.service.VisitCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;


@Tag(name = "短链接", description = "需要提供 JWT Token 或者 Guest-Id")
@SecurityRequirements // Swagger 不需要添加认证信息
@RequiredArgsConstructor
@RestController
@RequestMapping("/links")
public class LinkController {

    private final LinkService linkService;
    private final AccessRecordRepository accessRecordRepository;
    private final SecurityUserHelper securityUserHelper;
    private final VisitCountService visitCountService;

    @Operation(summary = "查询短链接", description = "获取短链接")
    @GetMapping
    public IPage<Link> query(LinkQuery filters, @Parameter(hidden = true) @Validated Pagination page) {
        return linkService.query(filters, page);
    }

    @Operation(summary = "获取短链接", description = "获取短链接")
    @ApiResponse(responseCode = "200", description = "短链接获取成功")
    @GetMapping("/{id}")
    public Link getById(@Parameter(description = "短链接 id") @PathVariable Long id) {
        return linkService.getById(id, securityUserHelper.getTypedUserIdOrThrow());
    }

    @Operation(summary = "生成短链接", description = "生成短链接")
    @ApiResponse(responseCode = "200", description = "短链接生成成功")
    @PostMapping
    public Link shorten(@RequestBody @Validated CreateLinkCommand command) throws AuthenticationException {
        String ownerId = securityUserHelper.getTypedUserIdOrThrow();
        return linkService.create(command, ownerId);
    }

    @Operation(summary = "删除短链接", description = "删除短链接")
    @DeleteMapping("/{id}")
    public void delete(@Parameter(description = "短链接 id")  @PathVariable Long id) {
        linkService.delete(id, securityUserHelper.getTypedUserIdOrThrow());
    }


    @Operation(summary = "获取短链接访问记录", description = "获取短链接访问记录")
    @ApiResponse(responseCode = "200", description = "短链接访问记录获取成功")
    @GetMapping("/{shortUrl}/access-records")
    public long accessRecords(@Parameter(description = "短链接") @PathVariable String shortUrl) {
        return visitCountService.getVisitCount(shortUrl);
    }

}
