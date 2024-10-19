package com.github.xioshe.less.url.api;


import com.github.xioshe.less.url.api.dto.MigrateResponse;
import com.github.xioshe.less.url.entity.User;
import com.github.xioshe.less.url.repository.UserRepository;
import com.github.xioshe.less.url.security.SecurityUser;
import com.github.xioshe.less.url.service.UserService;
import com.github.xioshe.less.url.util.constants.CustomHeaders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "用户")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Operation(summary = "获取当前用户信息，需要提供 token")
    @ApiResponse(responseCode = "200", description = "获取用户信息成功")
    @GetMapping("/me")
    public SecurityUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (SecurityUser) authentication.getPrincipal();
    }


    @Operation(summary = "根据 id 获取用户信息")
    @ApiResponse(responseCode = "200", description = "获取用户信息成功")
    @GetMapping("/{id}")
    public User getUserById(@Parameter(description = "用户 id") @PathVariable Long id) {
        return userRepository.getById(id);
    }

    @Operation(summary = "更新用户信息")
    @ApiResponse(responseCode = "200", description = "更新用户信息成功")
    @PutMapping
    public void updateUser(@RequestBody User user) {
        userService.update(user);
    }

    @Operation(summary = "迁移 Guest 数据到用户账户")
    @ApiResponse(responseCode = "200", description = "迁移用户数据成功")
    @PostMapping("/migrate")
    public MigrateResponse getCurrentGuestId(@AuthenticationPrincipal SecurityUser securityUser,
                                             @Parameter(in = ParameterIn.HEADER, description = "游客 id", required = true)
                                             @RequestHeader(value = CustomHeaders.GUEST_ID) String guestId) {
        return userService.migrate(guestId, securityUser);
    }

}
