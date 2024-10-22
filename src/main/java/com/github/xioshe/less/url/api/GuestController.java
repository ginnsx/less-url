package com.github.xioshe.less.url.api;


import com.github.xioshe.less.url.api.dto.MigrateResponse;
import com.github.xioshe.less.url.config.GuestAllowed;
import com.github.xioshe.less.url.security.SecurityUserHelper;
import com.github.xioshe.less.url.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "游客专属操作")
@GuestAllowed
@RestController
@RequestMapping("/guest")
@RequiredArgsConstructor
public class GuestController {

    private final LinkService linkService;
    private final SecurityUserHelper securityUserHelper;

    @Operation(summary = "统计 Guest 可迁移数据数量")
    @GetMapping("/counts")
    public MigrateResponse counts() {
        return linkService.countByOwner(securityUserHelper.getTypedUserIdOrThrow());
    }
}
