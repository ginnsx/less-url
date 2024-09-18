package com.github.xioshe.less.url.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.github.xioshe.less.url.config.SecurityConfig.ROLE_USER;

@Data
public class User implements UserDetails {
    private Long id;

    /**
     * 等级
     */
    private Byte level;

    private String username;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态
     */
    private Byte status;

    /**
     * 删除标识，主键
     */
    private Long deletedKey;

    /**
     * 邮箱
     */
    private String email;

    /**
     * API 请求密钥
     */
    private String apiKey;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ROLE_USER));
    }

    // Spring Security 要求
    // isAccountNonExpired(), isAccountNonLocked(),
    // isCredentialsNonExpired(),  isEnabled() 保持默认返回 true，否则相关用户无法登录
}