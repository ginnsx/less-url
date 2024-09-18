package com.github.xioshe.less.url.api.dto;

import com.github.xioshe.less.url.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class SignupCommand {

    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 邮箱
     */
    @Email
    @NotNull
    private String email;


    public User asUser(PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        return user;
    }
}
