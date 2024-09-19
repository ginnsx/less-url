package com.github.xioshe.less.url.api;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {


    @Secured("ROLE_USER")
    @GetMapping("/user")
    public String user() {
        return "hello, user";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/admin")
    public String admin() {
        return "hello, admin";
    }
}
