package com.github.xioshe.less.url.api;

import com.github.xioshe.less.url.security.RequirePermission;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


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
}
