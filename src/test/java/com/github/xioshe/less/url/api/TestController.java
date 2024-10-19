package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.security.RequirePermission;
import com.github.xioshe.less.url.security.SecurityUser;
import com.github.xioshe.less.url.security.SecurityUserHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
class TestController {

    @Autowired
    private SecurityUserHelper securityUserHelper;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String user() {
        return "hello, user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String admin() {
        return "hello, admin";
    }

    @RequirePermission(code = "test:edit", name = "编辑权限")
    @GetMapping("/edit")
    public String edit() {
        return "edit";
    }

    @PreAuthorize("hasRole('GUEST')")
    @GetMapping("/guest")
    public String guest(@AuthenticationPrincipal SecurityUser user) {
        return user.getUserId();
    }

    @GetMapping("/principal")
    public String guestPrincipal(@AuthenticationPrincipal Object principal) {
        return principal instanceof SecurityUser user ? user.getUsername() : principal.toString();
    }

    @GetMapping("/helper")
    public String helper() {
        return securityUserHelper.getTypedUserId().orElse("empty");
    }
}
