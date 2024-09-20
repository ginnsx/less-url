package com.github.xioshe.less.url.api;


import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "用户")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;


    @Operation(summary = "获取当前用户信息，需要提供 token")
    @ApiResponse(responseCode = "200", description = "获取用户信息成功")
    @GetMapping("/me")
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }


    @Operation(summary = "根据 id 获取用户信息")
    @ApiResponse(responseCode = "200", description = "获取用户信息成功")
    @GetMapping("/{id}")
    public User getUserById(@Parameter(description = "用户 id") @PathVariable Long id) {
        return userMapper.selectByPrimaryKey(id);
    }
}
