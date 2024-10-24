package com.github.xioshe.less.url.api;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xioshe.less.url.api.dto.CountLinkResponse;
import com.github.xioshe.less.url.api.dto.CreateLinkCommand;
import com.github.xioshe.less.url.api.dto.LinkQuery;
import com.github.xioshe.less.url.api.dto.Pagination;
import com.github.xioshe.less.url.config.GuestAllowed;
import com.github.xioshe.less.url.entity.Link;
import com.github.xioshe.less.url.security.SecurityUserHelper;
import com.github.xioshe.less.url.service.LinkService;
import com.github.xioshe.less.url.service.VisitCountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "短链接", description = "需要提供 JWT Token 或者 Guest-Id")
@SecurityRequirements // Swagger 不需要添加认证信息
@GuestAllowed // 允许游客访问
@RequiredArgsConstructor
@RestController
@RequestMapping("/links")
public class LinkController {

    private final LinkService linkService;
    private final SecurityUserHelper securityUserHelper;
    private final VisitCountService visitCountService;

    @Operation(summary = "查询短链接", description = "获取短链接")
    @GetMapping
    public IPage<Link> query(LinkQuery filters, @Parameter(hidden = true) @Validated Pagination page) {
        filters.setOwnerId(securityUserHelper.getTypedUserIdOrThrow());
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
    public Link shorten(@RequestBody @Validated CreateLinkCommand command) {
        String ownerId = securityUserHelper.getTypedUserIdOrThrow();
        return linkService.create(command, ownerId);
    }

    @Operation(summary = "删除短链接", description = "删除短链接")
    @DeleteMapping("/{id}")
    public void delete(@Parameter(description = "短链接 id")  @PathVariable Long id) {
        linkService.delete(id, securityUserHelper.getTypedUserIdOrThrow());
    }

    @Operation(summary = "根据认证用户，统计短链数量和点击记录数量")
    @GetMapping("/counts")
    public CountLinkResponse counts() {
        return linkService.countByOwner(securityUserHelper.getTypedUserIdOrThrow());
    }

    @Operation(summary = "统计短链接点击量")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/increase-count")
    public void increaseVisitCount() {
        linkService.updateVisitCount();
    }

}
