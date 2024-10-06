package com.github.xioshe.less.url.api.dto;

import com.github.xioshe.less.url.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Schema
@Data
public class SignupCommand {

    public static final String PASSWORD_REGEX = "^[A-Za-z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,16}$";


    @Schema(description = "验证码")
    @NotNull(message = "验证码不能为空")
    @Digits(integer = 6, fraction = 0, message = "验证码必须为 6 位数字")
    private String verifyCode;

    /**
     * 用户名
     */
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 16, message = "用户名长度为 4-16 位")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "密码")
    @NotNull(message = "密码不能为空")
    @Pattern(regexp = PASSWORD_REGEX, message = "密码由大小写字母、数字和特殊符号组成，长度为 8-16 位")
    private String password;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    @Email
    @NotNull(message = "邮箱不能为空")
    private String email;


    public User asUser(PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        return user;
    }
}
